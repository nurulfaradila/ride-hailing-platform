import axios from 'axios'
import { useState } from 'react'

const DRIVER_SERVICE_URL = 'http://localhost:8081'
const TRIP_SERVICE_URL = 'http://localhost:8082'

function AdminPage() {
    const [isPurging, setIsPurging] = useState(false)

    const purgeData = async () => {
        if (!window.confirm('This will delete all driver location registry and active trip history. Continue?')) return
        try {
            setIsPurging(true)
            await Promise.all([
                axios.delete(`${DRIVER_SERVICE_URL}/api/drivers/purge`),
                axios.delete(`${TRIP_SERVICE_URL}/api/trips/purge`)
            ])
            alert('Global System Reset Successful! All stale records cleared.')
        } catch (error) {
            console.error(error)
            alert('Failed to reset system data. Ensure all backend services are running.')
        } finally {
            setIsPurging(false)
        }
    }

    return (
        <div className="admin-page">
            <h2 style={{ marginBottom: '3rem' }}>System Settings</h2>

            <div className="layout-grid" style={{ gridTemplateColumns: '1fr' }}>
                <div className="glass-card">
                    <h3 style={{ marginBottom: '1.5rem', color: 'var(--accent)', textTransform: 'uppercase', fontSize: '1rem', letterSpacing: '1px' }}>Quick Start Guide</h3>
                    <ol style={{ marginLeft: '1.5rem', lineHeight: '2.5', color: 'var(--text-secondary)', fontFamily: 'var(--font-data)', fontSize: '0.9rem' }}>
                        <li>Use <strong>Reset All Data</strong> to clear simulation history.</li>
                        <li>Set a <strong>Driver Name</strong> in the Driver App.</li>
                        <li>Update your <strong>Location</strong> on the map.</li>
                        <li>Switch your status to <strong>Start Working</strong>.</li>
                        <li>Go to the <strong>Rider App</strong> to request a ride.</li>
                    </ol>
                </div>

                <div className="glass-card" style={{ borderColor: 'var(--accent-secondary)', background: 'rgba(255, 62, 62, 0.02)' }}>
                    <h3 style={{ marginBottom: '1rem', color: 'var(--accent-secondary)', textTransform: 'uppercase', fontSize: '1rem', letterSpacing: '1px' }}>Data Management</h3>
                    <p style={{ marginBottom: '2rem', fontSize: '0.9rem', color: 'var(--text-secondary)', lineHeight: '1.6' }}>
                        If you notice mismatched data or want to start a fresh simulation, use this tool to reset the entire system state.
                    </p>
                    <button
                        className="btn-primary"
                        onClick={purgeData}
                        disabled={isPurging}
                        style={{ background: 'var(--accent-secondary)' }}
                    >
                        {isPurging ? 'RESETTING SYSTEM...' : '🗑️ RESET ALL SYSTEM DATA'}
                    </button>
                </div>
            </div>
        </div>
    )
}

export default AdminPage
