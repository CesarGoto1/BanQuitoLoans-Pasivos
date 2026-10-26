import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import Aplicacion from './Aplicacion.jsx'
import './estilos/banquito.css'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Aplicacion />
    </BrowserRouter>
  </StrictMode>,
)