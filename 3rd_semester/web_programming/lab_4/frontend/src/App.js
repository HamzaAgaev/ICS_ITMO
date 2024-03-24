// import logo from './logo.svg'
// import './index.css'
import styles from './App.module.css'
import {useEffect, useState} from "react"
import {useNavigate} from 'react-router-dom'
import Form from 'react-bootstrap/Form'
import InputGroup from 'react-bootstrap/InputGroup'
import Button from 'react-bootstrap/Button'
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Container from 'react-bootstrap/Container'
import Navbar from 'react-bootstrap/Navbar'

function App() {

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()
  const handleRegister = () => {
    if (username.length < 3) {
      alert("Username is too short.")
      return
    }
    fetch('/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: username,
        passwordHash: password
      })
      
    }).then((res) => {
      if (res.status === 201) {
        const token = 'Basic ' + btoa(username + ':' + password)
        localStorage.setItem('token', token)
        navigate('/main')
      } else {
        alert("This user already exists!")
      }
    })
  }

  const handleLogin = () => {
    // const passwordHash = hash(password)
    const token = 'Basic ' + btoa(username + ':' + password)
    fetch('/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
    }).then((res) => {
      if (res.status === 200) {
        localStorage.setItem('token', token)
        navigate('/main')
      } else {
        if (res.status === 401)
          alert("This user doesn't exist.")
        else
          alert("Wrong password!")
      }
    })
  }

  return (
    <div className="App">
      <div className='wrapper'>

      <Navbar className="bg-body-tertiary">
      <Container>
        <Navbar.Brand href="#home">Агаев Хамза Рустам оглы</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse className="justify-content-end">
          <Navbar.Text>
          Вариант 33337 | Группа P3234
          </Navbar.Text>
        </Navbar.Collapse>
      </Container>
    </Navbar>

      <Form.Label htmlFor="inputPassword5">Username</Form.Label>
      <Form.Control
        type="text"
        // id="inputPassword5"
        // aria-describedby="passwordHelpBlock"
        value={username} onChange={(e)=>{setUsername(e.target.value)}}
      />



      <Form.Label htmlFor="inputPassword5">Password</Form.Label>
      <Form.Control
        type="password"
        // id="inputPassword5"
        // aria-describedby="passwordHelpBlock"
        value={password} onChange={(e)=>{setPassword(e.target.value)}}
      />
            
      <ButtonGroup size="lg" className="mb-2">
        <Button onClick={handleLogin}>Log in</Button>
        <Button onClick={handleRegister}>Register</Button>
      </ButtonGroup>

      </div>
      
    </div>
  )
}

export default App
