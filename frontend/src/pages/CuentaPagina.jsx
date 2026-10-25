import { useEffect, useState } from 'react';
import { getAccounts, getMovements } from '../services/pasivosApi.js';
import { getSession } from '../services/sesion.js';

const PAGE_SIZE = 10;

const currencyFormatter = new Intl.NumberFormat('es-EC', {
  style: 'currency',
  currency: 'USD',
});

function formatCurrency(value) {
  return currencyFormatter.format(value);
}

function formatDate(value) {
  if (!value) {
    return '';
  }
  const [year, month, day] = value.substring(0, 10).split('-');
  return `${day}/${month}/${year}`;
}

const STATUS_CLASS = {
  ACTIVA: 'bq-estado-exito',
  INACTIVA: 'bq-estado-aviso',
  CERRADA: 'bq-estado-peligro',
};

export default function CuentaPagina() {
  const identificationNumber = getSession().identificationNumber;
  const [accounts, setAccounts] = useState([]);
  const [selectedNumber, setSelectedNumber] = useState('');
  const [movements, setMovements] = useState(null);
  const [page, setPage] = useState(0);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAccounts(identificationNumber)
      .then((data) => {
        setAccounts(data);
        if (data.length > 0) {
          setSelectedNumber(data[0].accountNumber);
        }
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [identificationNumber]);

  useEffect(() => {
    if (!selectedNumber) {
      return;
    }
    setError('');
    getMovements(selectedNumber, page, PAGE_SIZE)
      .then(setMovements)
      .catch((err) => setError(err.message));
  }, [selectedNumber, page]);

  function handleSelect(accountNumber) {
    setMovements(null);
    setPage(0);
    setSelectedNumber(accountNumber);
  }

  const account = accounts.find((item) => item.accountNumber === selectedNumber);

  const pageInfo = movements ? movements.page ?? movements : null;
  const currentPage = pageInfo?.number ?? page;
  const totalPages = pageInfo?.totalPages ?? 1;

  return (
    <main className="bq-pagina">
      <div className="bq-titulo-pagina">
        <div>
          <h1>Mi cuenta</h1>
          <p>Saldo y movimientos de tus cuentas</p>
        </div>
      </div>

      {error && (
        <div className="bq-alerta bq-alerta-peligro" role="alert">
          <strong>Ocurrió un problema</strong>
          <span>{error}</span>
        </div>
      )}

      {loading && <p className="bq-texto-2">Cargando...</p>}

      {!loading && !error && accounts.length === 0 && (
        <div className="bq-alerta bq-alerta-info">
          <strong>Sin cuentas</strong>
          <span>No tienes cuentas registradas.</span>
        </div>
      )}

      {accounts.length > 1 && (
        <div className="bq-segmentado">
          {accounts.map((item) => (
            <button
              key={item.accountNumber}
              type="button"
              aria-pressed={item.accountNumber === selectedNumber}
              onClick={() => handleSelect(item.accountNumber)}
            >
              {item.accountNumber}
            </button>
          ))}
        </div>
      )}

      {account && (
        <div className="bq-fila">
          <div className="bq-tarjeta-destacada">
            <span className="bq-etiqueta-seccion">Saldo</span>
            <span className="bq-monto-grande">{formatCurrency(account.balance)}</span>
          </div>
          <div className="bq-tarjeta">
            <div className="bq-datos">
              <div className="bq-dato">
                <span>Número de cuenta</span>
                <span>{account.accountNumber}</span>
              </div>
              <div className="bq-dato">
                <span>Tipo</span>
                <span>{account.type}</span>
              </div>
              <div className="bq-dato">
                <span>Fecha de apertura</span>
                <span>{formatDate(account.openingDate)}</span>
              </div>
              <div className="bq-dato">
                <span>Estado</span>
                <span className={`bq-estado ${STATUS_CLASS[account.status] || 'bq-estado-neutro'}`}>
                  {account.status}
                </span>
              </div>
            </div>
          </div>
        </div>
      )}

      {account && <h2>Movimientos</h2>}

      {movements && movements.content.length === 0 && (
        <div className="bq-alerta bq-alerta-info">
          <strong>Sin movimientos</strong>
          <span>Esta cuenta aún no tiene movimientos.</span>
        </div>
      )}

      {movements && movements.content.length > 0 && (
        <>
          <div className="bq-tabla-contenedor">
            <table className="bq-tabla">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Concepto</th>
                  <th>Tipo</th>
                  <th className="bq-num">Monto</th>
                  <th className="bq-num">Saldo posterior</th>
                  <th>Operación</th>
                  <th>Canal</th>
                </tr>
              </thead>
              <tbody>
                {movements.content.map((movement) => (
                  <tr key={movement.transactionUuid}>
                    <td>{formatDate(movement.movementDate)}</td>
                    <td>{movement.concept}</td>
                    <td>{movement.type}</td>
                    <td className="bq-num">{formatCurrency(movement.amount)}</td>
                    <td className="bq-num">{formatCurrency(movement.balanceAfter)}</td>
                    <td>{movement.operationNumber}</td>
                    <td>{movement.channel}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="bq-acciones">
            <span className="bq-texto-2">
              Página {currentPage + 1} de {totalPages}
            </span>
            <button
              type="button"
              className="bq-btn bq-btn-secundario bq-btn-sm"
              disabled={currentPage <= 0}
              onClick={() => setPage(page - 1)}
            >
              Anterior
            </button>
            <button
              type="button"
              className="bq-btn bq-btn-secundario bq-btn-sm"
              disabled={currentPage + 1 >= totalPages}
              onClick={() => setPage(page + 1)}
            >
              Siguiente
            </button>
          </div>
        </>
      )}
    </main>
  );
}