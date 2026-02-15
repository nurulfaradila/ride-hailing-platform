import { useState } from 'react'
import axios from 'axios'
import Map from '../components/Map'

const DRIVER_SERVICE_URL = 'http://localhost:8081'
const TRIP_SERVICE_URL = 'http://localhost:8082'

function DriverPage({ driverId, setDriverId, status, setStatus, location, setLocation, isSyncing, setIsSyncing }) {
    const [currentTrip, setCurrentTrip] = useState(null);

    const toggleStatus = async () => {
        const newStatus = status === 'OFFLINE' ? 'AVAILABLE' : 'OFFLINE'
        try {
            await axios.patch(`${DRIVER_SERVICE_URL}/api/drivers/${driverId}/status?status=${newStatus}`)
            setStatus(newStatus)
        } catch (error) {
            console.error('Failed to update status', error)
            alert('Failed to update online status. Check Driver Service.')
        }
    }

    const updateLocation = async () => {
        try {
            setIsSyncing(true)
            await axios.patch(`${DRIVER_SERVICE_URL}/api/drivers/${driverId}/location`, {
                lat: location.lat,
                lng: location.lng
            })
            setTimeout(() => setIsSyncing(false), 800)
            // Check for assigned trips
            checkForTrips();
        } catch (error) {
            console.error(error)
            setIsSyncing(false)
            alert('Sync failed. Ensure Driver Service is running on port 8081.')
        }
    }

    const checkForTrips = async () => {
        // In a real app, this would be a websocket or polling.
    }

    // Mock receiving a trip - in reality this comes from WebSocket or Polling
    const [manualTripId, setManualTripId] = useState('');

    const completeTrip = async () => {
        if (!manualTripId) return alert('Enter Trip ID first');
        try {
            await axios.post(`${TRIP_SERVICE_URL}/api/trips/${manualTripId}/complete`)
            alert('Trip Completed!')
            setManualTripId('')
        } catch (error) {
            console.error(error)
            const msg = error.response?.data || error.message
            alert(`Failed to complete trip: ${msg}`)
        }
    }

    const cancelTrip = async () => {
        if (!manualTripId) return alert('Enter Trip ID first');
        try {
            await axios.post(`${TRIP_SERVICE_URL}/api/trips/${manualTripId}/cancel`)
            alert('Trip Cancelled!')
            setManualTripId('')
        } catch (error) {
            console.error(error)
            const msg = error.response?.data || error.message
            alert(`Failed to cancel trip: ${msg}`)
        }
    }

    const handleMapClick = (latlng) => {
        setLocation({ lat: latlng.lat, lng: latlng.lng });
    }

    const markers = [
        { lat: location.lat, lng: location.lng, label: 'My Location', type: 'driver' }
    ]

    return (
        <div className="driver-page">
            <div className="map-container">
                <Map center={[location.lat, location.lng]} markers={markers} onMapClick={handleMapClick} />
            </div>

            <div className="form-group">
                <label>Driver Identity</label>
                <input
                    type="text"
                    value={driverId}
                    onChange={e => setDriverId(e.target.value)}
                />
            </div>

            <div className="card" style={{ marginBottom: '20px' }}>
                <h3>Status & Location</h3>
                <div style={{ display: 'flex', gap: '12px', alignItems: 'center', marginBottom: '16px' }}>
                    <div className={`status-badge ${status === 'AVAILABLE' ? 'active' : ''}`}>{status}</div>
                    <button className="btn-outline" onClick={toggleStatus} style={{ flex: 1 }}>
                        {status === 'OFFLINE' ? 'Go Online' : 'Go Offline'}
                    </button>
                    <button className="btn-outline" onClick={updateLocation} disabled={isSyncing}>
                        {isSyncing ? '...' : 'Global Sync'}
                    </button>
                </div>
                <div className="form-row">
                    <div style={{ flex: 1 }}>
                        <label>Lat</label>
                        <input type="number" value={location.lat.toFixed(4)} readOnly />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Lng</label>
                        <input type="number" value={location.lng.toFixed(4)} readOnly />
                    </div>
                </div>
            </div>

            <div className="card">
                <h3>Active Job Controls</h3>
                <div className="form-group">
                    <label>Trip ID (Paste Here)</label>
                    <input
                        type="text"
                        value={manualTripId}
                        onChange={e => setManualTripId(e.target.value)}
                        placeholder="UUID from Rider App"
                        style={{ fontFamily: 'monospace' }}
                    />
                </div>
                <div className="driver-controls">
                    <button
                        className="btn-primary"
                        onClick={completeTrip}
                    >
                        ✅ Complete
                    </button>
                    <button
                        className="btn-outline"
                        style={{ borderColor: '#ef4444', color: '#ef4444' }}
                        onClick={cancelTrip}
                    >
                        ✕ Cancel
                    </button>
                </div>
            </div>
        </div>
    )
}

export default DriverPage
