-- =====================================================================
-- BanQuito · Datos de prueba de cartera (corePasivos)
-- Ejecutar DESPUÉS de corePasivos.sql y datos-simulados-pasivos.sql.
-- Acredita el desembolso neto de las 7 operaciones de prueba de
-- datos-pruebas-cartera-prestamos.sql en la cuenta de cada cliente.
-- =====================================================================

BEGIN;

-- OP-20261001-009001 · Al día, recién desembolsado (sin cuotas vencidas)
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',4975.00,"saldoCuenta"+4975.00,'2026-10-01','2026-10-01 11:00','OP-20261001-009001','Desembolso OP-20261001-009001','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000020';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+4975.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000020';

-- OP-20260814-009002 · Al día con 2 cuotas pagadas
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',7960.00,"saldoCuenta"+7960.00,'2026-08-14','2026-08-14 11:00','OP-20260814-009002','Desembolso OP-20260814-009002','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000021';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+7960.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000021';

-- OP-20260916-009003 · 15 días de mora (tramo 2, VENCIDA 5 %)
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',5970.00,"saldoCuenta"+5970.00,'2026-09-16','2026-09-16 11:00','OP-20260916-009003','Desembolso OP-20260916-009003','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000022';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+5970.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000022';

-- OP-20260816-009004 · 45 días de mora, 2 cuotas vencidas (tramo 3, VENCIDA 25 %)
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',9950.00,"saldoCuenta"+9950.00,'2026-08-16','2026-08-16 11:00','OP-20260816-009004','Desembolso OP-20260816-009004','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000023';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+9950.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000023';

-- OP-20260920-009005 · 11 días de mora con la cuota 1 pagada en parte
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',3980.00,"saldoCuenta"+3980.00,'2026-09-20','2026-09-20 11:00','OP-20260920-009005','Desembolso OP-20260920-009005','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000024';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+3980.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000024';

-- OP-20260601-009006 · 122 días de mora (tramo 4, EN_LITIGIO 60 %)
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',11940.00,"saldoCuenta"+11940.00,'2026-06-01','2026-06-01 11:00','OP-20260601-009006','Desembolso OP-20260601-009006','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000025';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+11940.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000025';

-- OP-20260315-009007 · 199 días de mora (tramo 5, CASTIGADA 100 %)
INSERT INTO "movimientos" ("idCuenta","tipoMovimiento","conceptoMovimiento","montoMovimiento","saldoPosterior","fechaMovimiento","fechaHoraMovimiento","numeroOperacion","referencia","usuarioCreacion")
SELECT "idCuenta",'CREDITO','DESEMBOLSO',2985.00,"saldoCuenta"+2985.00,'2026-03-15','2026-03-15 11:00','OP-20260315-009007','Desembolso OP-20260315-009007','core-activos' FROM "cuentas" WHERE "numeroCuenta"='2200000026';
UPDATE "cuentas" SET "saldoCuenta"="saldoCuenta"+2985.00,"usuarioModificacion"='core-activos',"fechaModificacion"=CURRENT_TIMESTAMP WHERE "numeroCuenta"='2200000026';

COMMIT;
