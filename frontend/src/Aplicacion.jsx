import { Navigate, Route, Routes } from 'react-router-dom'
import InicioSesionPagina from './paginas/InicioSesionPagina.jsx'
import MenuPagina from './paginas/MenuPagina.jsx'
import CuentaPagina from './paginas/CuentaPagina.jsx'

export default function Aplicacion() {
  return (
    <Routes>
      <Route path="/" element={<InicioSesionPagina />} />
      <Route path="/banca" element={<MenuPagina />}>
        <Route index element={<Navigate to="mi-cuenta" replace />} />
        <Route path="mi-cuenta" element={<CuentaPagina />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}