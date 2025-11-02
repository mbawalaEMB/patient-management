import { useEffect, useState } from 'react'
import './App.css'

function App() {
  const [patients, setPatients] = useState([])

  useEffect(() => {
    fetch('http://localhost:4000/patients')
      .then((res) => res.json())
      .then((data) => setPatients(data))
      .catch((err) => console.error("Error fetching patients :", err));
  }, []);

  return (
    <>
      <div>
        <h1>Patient List</h1>
        {patients.length === 0 ? (<p>Loading...</p>): (
          <table border="1" cellPadding="8">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Address</th>
                <th>Date of Birth</th>
              </tr>
            </thead>
            <tbody>
              {
                patients.map((p, i) => (
                  <tr key={i}>
                    <td>{p.name}</td>
                    <td>{p.email}</td>
                    <td>{p.address}</td>
                    <td>{p.dateOfBirth}</td>
                  </tr>
                ))
              }
            </tbody>
          </table>
        )}
      </div>
      
    </>
  )
}

export default App
