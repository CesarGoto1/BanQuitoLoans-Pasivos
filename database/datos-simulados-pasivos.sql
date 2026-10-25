WITH base AS (
    SELECT n,
           '17' || ((n % 6))::text || lpad((100000 + n * 7)::text, 6, '0') AS nueve
      FROM generate_series(1, 300) n
), verificador AS (
    SELECT n, nueve,
           (10 - (sum(CASE WHEN p % 2 = 1
                           THEN CASE WHEN substr(nueve, p, 1)::int * 2 > 9 THEN substr(nueve, p, 1)::int * 2 - 9
                                     ELSE substr(nueve, p, 1)::int * 2 END
                           ELSE substr(nueve, p, 1)::int END) % 10)) % 10 AS dv
      FROM base, generate_series(1, 9) p
     GROUP BY n, nueve
)
INSERT INTO "clientes" ("cedulaCliente", "nombresCliente", "apellidosCliente", "fechaNacimiento", "direccionCliente",
                        "ciudadCliente", "telefonoCliente", "correoCliente", "estadoCliente", "usuarioCreacion")
SELECT v.nueve || v.dv,
       (ARRAY['Ana','Luis','María','Carlos','Sofía','Jorge','Valeria','Andrés','Camila','Diego',
              'Gabriela','José','Daniela','Pablo','Fernanda','Miguel','Paola','Santiago','Lucía','Mateo'])[1 + (v.n % 20)]
       || ' ' ||
       (ARRAY['Isabel','Alejandro','Elena','Javier','Patricia','Esteban','Carolina','Ricardo','Natalia','Sebastián'])[1 + (v.n % 10)],
       (ARRAY['Pérez','González','Rodríguez','Torres','Vásquez','Morales','Castillo','Herrera','Mendoza','Salazar',
              'Guerrero','Ortiz','Cevallos','Andrade','Paredes','Espinoza','Villacís','Jaramillo','Benítez','Montalvo'])[1 + (v.n * 7 % 20)]
       || ' ' ||
       (ARRAY['López','Ramírez','Flores','Chávez','Ruiz','Navarro','Molina','Suárez','Aguirre','Proaño'])[1 + (v.n * 3 % 10)],
       DATE '1960-01-15' + ((v.n * 137) % 14600),
       (ARRAY['Av. Amazonas','Av. 10 de Agosto','Av. Shyris','Calle García Moreno','Av. 6 de Diciembre',
              'Av. América','Calle Venezuela','Av. Eloy Alfaro'])[1 + (v.n % 8)] || ' N' || (10 + v.n % 80) || '-' || (10 + v.n % 90),
       (ARRAY['Quito','Quito','Quito','Guayaquil','Cuenca','Ambato','Loja','Ibarra'])[1 + (v.n % 8)],
       '09' || lpad(((v.n * 7919) % 100000000)::text, 8, '0'),
       CASE WHEN v.n % 7 = 0 THEN NULL ELSE 'cliente' || v.n || '@correo.ec' END,
       CASE WHEN v.n % 50 = 0 THEN 'INACTIVO' ELSE 'ACTIVO' END,
       'datos-simulados'
  FROM verificador v
 ORDER BY v.n;

INSERT INTO "cuentas" ("idCliente", "numeroCuenta", "tipoCuenta", "saldoCuenta", "fechaApertura", "estadoCuenta", "usuarioCreacion")
SELECT c."idCliente", '22' || lpad(c."idCliente"::text, 8, '0'), 'AHORROS',
       0, DATE '2015-01-05' + ((c."idCliente" * 53) % 4000),
       CASE WHEN c."estadoCliente" = 'INACTIVO' THEN 'INACTIVA' ELSE 'ACTIVA' END, 'datos-simulados'
  FROM "clientes" c
UNION ALL
SELECT c."idCliente", '33' || lpad(c."idCliente"::text, 8, '0'), 'CORRIENTE',
       0, DATE '2018-03-01' + ((c."idCliente" * 31) % 2500), 'ACTIVA', 'datos-simulados'
  FROM "clientes" c
 WHERE c."idCliente" % 5 = 0 AND c."estadoCliente" = 'ACTIVO'
 ORDER BY 2;

WITH mov AS (
    SELECT cu."idCuenta", k,
           CASE WHEN k % 2 = 1 THEN 'CREDITO' ELSE 'DEBITO' END AS tipo,
           CASE WHEN k % 2 = 1 THEN 'DEPOSITO' ELSE 'RETIRO' END AS concepto,
           CASE k WHEN 1 THEN 800 + (cu."idCuenta" * 97) % 4200
                  WHEN 2 THEN 100 + (cu."idCuenta" * 13) % 300
                  WHEN 3 THEN 300 + (cu."idCuenta" * 41) % 1500
                  ELSE        50  + (cu."idCuenta" * 17) % 400 END::numeric(12,2) AS monto,
           DATE '2026-10-31' - (120 - k * 25) AS fecha
      FROM "cuentas" cu, generate_series(1, 4) k
), con_saldo AS (
    SELECT m.*, sum(CASE WHEN tipo = 'CREDITO' THEN monto ELSE -monto END)
                    OVER (PARTITION BY "idCuenta" ORDER BY k) AS saldo
      FROM mov m
)
INSERT INTO "movimientos" ("idCuenta", "tipoMovimiento", "conceptoMovimiento", "montoMovimiento", "saldoPosterior",
                           "fechaMovimiento", "fechaHoraMovimiento", "uuidTransaccion", "referencia", "usuarioCreacion")
SELECT "idCuenta", tipo, concepto, monto, saldo, fecha, fecha + TIME '10:00' + ("idCuenta" % 300) * INTERVAL '1 minute',
       md5('mov-' || "idCuenta" || '-' || k)::uuid,
       CASE concepto WHEN 'DEPOSITO' THEN 'Depósito en ventanilla' ELSE 'Retiro en cajero' END, 'datos-simulados'
  FROM con_saldo
 ORDER BY "idCuenta", k;

UPDATE "cuentas" cu SET "saldoCuenta" = m."saldoPosterior"
  FROM (SELECT DISTINCT ON ("idCuenta") "idCuenta", "saldoPosterior" FROM "movimientos"
         ORDER BY "idCuenta", "fechaMovimiento" DESC, "idMovimiento" DESC) m
 WHERE m."idCuenta" = cu."idCuenta";
