import { useState, useEffect } from 'react'
import axios from 'axios'
import Map from '../components/Map'

const TRIP_SERVICE_URL = 'http://localhost:8082'

function RiderPage({ riderId, setRiderId, pickup, setPickup, dropoff, setDropoff, trip, setTrip, status, setStatus }) {

    const [selectionMode, setSelectionMode] = useState('pickup') // 'pickup' or 'dropoff'

    const handleMapClick = (latlng) => {
        if (selectionMode === 'pickup') {
            setPickup({ lat: latlng.lat, lng: latlng.lng });
        } else {
            setDropoff({ lat: latlng.lat, lng: latlng.lng });
        }
    }

    const requestRide = async () => {
        try {
            setStatus('SEARCHING')
            const response = await axios.post(`${TRIP_SERVICE_URL}/api/trips`, {
                riderId,
                pickupLat: pickup.lat,
                pickupLng: pickup.lng,
                dropoffLat: dropoff.lat,
                dropoffLng: dropoff.lng
            })
            setTrip(response.data)
        } catch (error) {
            console.error(error)
            setStatus('ERROR')
        }
    }

    const cancelRide = async () => {
        if (!trip) return;
        try {
            await axios.post(`${TRIP_SERVICE_URL}/api/trips/${trip.id}/cancel`)
            // The polling effect will catch the status update
        } catch (error) {
            console.error('Failed to cancel ride', error)
            const msg = error.response?.data || error.message
            alert(`Failed to cancel ride: ${msg}`)
        }
    }

    useEffect(() => {
        let interval
        if (trip && trip.status !== 'COMPLETED' && trip.status !== 'CANCELLED') {
            interval = setInterval(async () => {
                try {
                    const response = await axios.get(`${TRIP_SERVICE_URL}/api/trips/${trip.id}`)
                    setTrip(response.data)
                    setStatus(response.data.status)
                } catch (error) {
                    console.error('Polling error', error)
                }
            }, 3000)
        }
        return () => clearInterval(interval)
    }, [trip])

    const markers = [
        { lat: pickup.lat, lng: pickup.lng, label: 'Pickup', type: 'rider' },
        { lat: dropoff.lat, lng: dropoff.lng, label: 'Destination', type: 'destination' }
    ]

    return (
        <div className="rider-page">
            <div className="map-container">
                <Map center={[pickup.lat, pickup.lng]} markers={markers} onMapClick={handleMapClick} />
            </div>

            <div className="form-group">
                <label>Rider Identity</label>
                <input
                    type="text"
                    value={riderId}
                    onChange={e => setRiderId(e.target.value)}
                    placeholder="e.g. rider-123"
                />
            </div>

            <div className="form-row">
                <div style={{ flex: 1 }}>
                    <label>Pickup</label>
                    <div style={{ display: 'flex', gap: '8px' }}>
                        <input type="number" readOnly value={pickup.lat.toFixed(4)} />
                        <input type="number" readOnly value={pickup.lng.toFixed(4)} />
                    </div>
                    <button className={`btn-sm ${selectionMode === 'pickup' ? 'active' : ''}`} style={{ marginTop: '8px', width: '100%', borderColor: selectionMode === 'pickup' ? 'var(--accent-rider)' : '' }} onClick={() => setSelectionMode('pickup')}>
                        📍 Set on Map
                    </button>
                </div>
                <div style={{ flex: 1 }}>
                    <label>Destination</label>
                    <div style={{ display: 'flex', gap: '8px' }}>
                        <input type="number" readOnly value={dropoff.lat.toFixed(4)} />
                        <input type="number" readOnly value={dropoff.lng.toFixed(4)} />
                    </div>
                    <button className={`btn-sm ${selectionMode === 'dropoff' ? 'active' : ''}`} style={{ marginTop: '8px', width: '100%', borderColor: selectionMode === 'dropoff' ? 'var(--accent-rider)' : '' }} onClick={() => setSelectionMode('dropoff')}>
                        🏁 Set on Map
                    </button>
                </div>
            </div>

            <button
                className="btn-primary"
                onClick={requestRide}
                disabled={status === 'SEARCHING' || (trip && trip.status !== 'COMPLETED' && trip.status !== 'CANCELLED')}
            >
                {status === 'SEARCHING' ? '✨ Finding Driver...' : '⚡ REQUEST RIDE'}
            </button>

            {trip && (
                <div className="card" style={{ marginTop: '20px' }}>
                    <h3>Trip Details</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '16px' }}>
                        <div>
                            <label>Distance</label>
                            <div style={{ fontSize: '1.1rem', fontWeight: 'bold' }}>{trip.distance || '-'} km</div>
                        </div>
                        <div>
                            <label>Est. Price</label>
                            <div style={{ fontSize: '1.1rem', fontWeight: 'bold' }}>${trip.estimatedPrice || '-'}</div>
                        </div>
                    </div>

                    <div style={{ marginBottom: '16px' }}>
                        <label>Trip ID</label>
                        <div style={{ fontFamily: 'monospace', fontSize: '0.8rem', color: 'var(--text-muted)', wordBreak: 'break-all' }}>{trip.id}</div>
                    </div>

                    <div style={{ marginBottom: '16px' }}>
                        <label>Status</label>
                        <span className={`status-badge active`}>{trip.status}</span>
                    </div>

                    {(trip.status === 'REQUESTED' || trip.status === 'ASSIGNED') && (
                        <button className="btn-outline" style={{ width: '100%', borderColor: 'var(--accent-secondary)', color: 'var(--accent-secondary)' }} onClick={cancelRide}>
                            ✕ Cancel Ride
                        </button>
                    )}
                </div>
            )}
        </div>
    )
}

export default RiderPage
