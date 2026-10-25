import { Navigate, Route, Routes } from 'react-router-dom'
import LoginPagina from './pages/LoginPagina.jsx'
import MenuPagina from './pages/MenuPagina.jsx'
import CuentaPagina from './pages/CuentaPagina.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginPagina />} />
      <Route path="/banca" element={<MenuPagina />}>
        <Route index element={<Navigate to="mi-cuenta" replace />} />
        <Route path="mi-cuenta" element={<CuentaPagina />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}