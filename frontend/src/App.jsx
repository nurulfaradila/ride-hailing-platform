import { useState, useEffect } from 'react'
import RiderPage from './pages/RiderPage'
import DriverPage from './pages/DriverPage'
import AdminPage from './pages/AdminPage'

function App() {
    // Shared State for Demo
    const [riderId, setRiderId] = useState('rider-123')
    const [pickup, setPickup] = useState({ lat: 1.290270, lng: 103.851959 })
    const [dropoff, setDropoff] = useState({ lat: 1.300270, lng: 103.861959 })
    const [trip, setTrip] = useState(null)
    const [riderStatus, setRiderStatus] = useState('')

    const [driverId, setDriverId] = useState('driver-007')
    const [driverStatus, setDriverStatus] = useState('OFFLINE')
    const [driverLocation, setDriverLocation] = useState({ lat: 1.290270, lng: 103.851959 })
    const [isSyncing, setIsSyncing] = useState(false)

    useEffect(() => {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition((position) => {
                const { latitude, longitude } = position.coords;
                setPickup({ lat: latitude, lng: longitude });
                setDriverLocation({ lat: latitude, lng: longitude });
                setDropoff({ lat: latitude + 0.01, lng: longitude + 0.01 });
            }, (error) => {
                console.warn("Geolocation permission denied or error:", error.message);
            });
        }
    }, []);

    return (
        <div className="dashboard-container">
            {/* Rider Panel */}
            <div className="app-panel theme-rider">
                <div className="panel-header">
                    <div className="panel-title">
                        <span>👤</span> RIDER APP
                    </div>
                    <div className={`status-badge ${riderStatus === 'SEARCHING' ? 'active' : ''}`}>
                        {riderStatus || 'IDLE'}
                    </div>
                </div>
                <div className="panel-content">
                    <RiderPage
                        riderId={riderId} setRiderId={setRiderId}
                        pickup={pickup} setPickup={setPickup}
                        dropoff={dropoff} setDropoff={setDropoff}
                        trip={trip} setTrip={setTrip}
                        status={riderStatus} setStatus={setRiderStatus}
                    />
                </div>
            </div>

            {/* Driver Panel */}
            <div className="app-panel theme-driver">
                <div className="panel-header">
                    <div className="panel-title">
                        <span>🚘</span> DRIVER APP
                    </div>
                    <div className={`status-badge ${driverStatus === 'AVAILABLE' ? 'active' : ''}`}>
                        {driverStatus}
                    </div>
                </div>
                <div className="panel-content">
                    <DriverPage
                        driverId={driverId} setDriverId={setDriverId}
                        status={driverStatus} setStatus={setDriverStatus}
                        location={driverLocation} setLocation={setDriverLocation}
                        isSyncing={isSyncing} setIsSyncing={setIsSyncing}
                    />
                </div>
            </div>
        </div>
    )
}

export default App
