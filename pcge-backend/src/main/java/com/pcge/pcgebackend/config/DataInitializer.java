package com.pcge.pcgebackend.config;

import org.springframework.boot.CommandLineRunner;
import com.pcge.pcgebackend.model.Cuenta;
import com.pcge.pcgebackend.repository.CuentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CuentaRepository cuentaRepository;

    public DataInitializer(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen cuentas para no duplicar
        if (cuentaRepository.count() == 0) {
            System.out.println("Cargando cuentas contables básicas...");
            cargarCuentasBasicas();
            System.out.println("Cuentas cargadas exitosamente!");
        } else {
            System.out.println("Las cuentas ya existen en la base de datos.");
        }
    }

    private void cargarCuentasBasicas() {
        // ACTIVO
        Cuenta activo = new Cuenta();
        activo.setCodigo("10");
        activo.setNombre("ACTIVO");
        activo.setNivel(1);
        activo.setTipo("ACTIVO");
        activo.setPadreId(null);
        cuentaRepository.save(activo);

        Cuenta caja = new Cuenta();
        caja.setCodigo("121");
        caja.setNombre("CAJA");
        caja.setNivel(2);
        caja.setTipo("ACTIVO");
        caja.setPadreId("101");
        cuentaRepository.save(caja);

        Cuenta cajaGeneral = new Cuenta();
        cajaGeneral.setCodigo("1011");
        cajaGeneral.setNombre("Caja General");
        cajaGeneral.setNivel(3);
        cajaGeneral.setTipo("ACTIVO");
        cajaGeneral.setPadreId("101");
        cuentaRepository.save(cajaGeneral);

        Cuenta bancos = new Cuenta();
        bancos.setCodigo("104");
        bancos.setNombre("CUENTAS CORRIENTES");
        bancos.setNivel(2);
        bancos.setTipo("ACTIVO");
        bancos.setPadreId("10");
        cuentaRepository.save(bancos);

        // PASIVO
        Cuenta pasivo = new Cuenta();
        pasivo.setCodigo("20");
        pasivo.setNombre("PASIVO");
        pasivo.setNivel(1);
        pasivo.setTipo("PASIVO");
        pasivo.setPadreId(null);
        cuentaRepository.save(pasivo);

        Cuenta tributos = new Cuenta();
        tributos.setCodigo("40");
        tributos.setNombre("TRIBUTOS POR PAGAR");
        tributos.setNivel(1);
        tributos.setTipo("PASIVO");
        tributos.setPadreId(null);
        cuentaRepository.save(tributos);

        Cuenta gobiernoCentral = new Cuenta();
        gobiernoCentral.setCodigo("401");
        gobiernoCentral.setNombre("Gobierno Central");
        gobiernoCentral.setNivel(2);
        gobiernoCentral.setTipo("PASIVO");
        gobiernoCentral.setPadreId("40");
        cuentaRepository.save(gobiernoCentral);

        Cuenta igv = new Cuenta();
        igv.setCodigo("4011");
        igv.setNombre("IGV");
        igv.setNivel(3);
        igv.setTipo("PASIVO");
        igv.setPadreId("401");
        cuentaRepository.save(igv);

        Cuenta igvPorPagar = new Cuenta();
        igvPorPagar.setCodigo("40111");
        igvPorPagar.setNombre("IGV por Pagar");
        igvPorPagar.setNivel(4);
        igvPorPagar.setTipo("PASIVO");
        igvPorPagar.setPadreId("4011");
        cuentaRepository.save(igvPorPagar);

        // PATRIMONIO
        Cuenta patrimonio = new Cuenta();
        patrimonio.setCodigo("50");
        patrimonio.setNombre("PATRIMONIO");
        patrimonio.setNivel(1);
        patrimonio.setTipo("PATRIMONIO");
        patrimonio.setPadreId(null);
        cuentaRepository.save(patrimonio);

        // INGRESOS
        Cuenta ingresos = new Cuenta();
        ingresos.setCodigo("70");
        ingresos.setNombre("VENTAS");
        ingresos.setNivel(1);
        ingresos.setTipo("INGRESO");
        ingresos.setPadreId(null);
        cuentaRepository.save(ingresos);

        Cuenta ventasMercaderias = new Cuenta();
        ventasMercaderias.setCodigo("701");
        ventasMercaderias.setNombre("Mercaderías");
        ventasMercaderias.setNivel(2);
        ventasMercaderias.setTipo("INGRESO");
        ventasMercaderias.setPadreId("70");
        cuentaRepository.save(ventasMercaderias);

        Cuenta ventaMercaderias = new Cuenta();
        ventaMercaderias.setCodigo("7011");
        ventaMercaderias.setNombre("Venta de Mercaderías");
        ventaMercaderias.setNivel(3);
        ventaMercaderias.setTipo("INGRESO");
        ventaMercaderias.setPadreId("701");
        cuentaRepository.save(ventaMercaderias);

        // GASTOS
        Cuenta gastos = new Cuenta();
        gastos.setCodigo("90");
        gastos.setNombre("GASTOS");
        gastos.setNivel(1);
        gastos.setTipo("GASTO");
        gastos.setPadreId(null);
        cuentaRepository.save(gastos);

        Cuenta gastosAdministrativos = new Cuenta();
        gastosAdministrativos.setCodigo("91");
        gastosAdministrativos.setNombre("GASTOS ADMINISTRATIVOS");
        gastosAdministrativos.setNivel(2);
        gastosAdministrativos.setTipo("GASTO");
        gastosAdministrativos.setPadreId("90");
        cuentaRepository.save(gastosAdministrativos);

        System.out.println("Total de cuentas cargadas: " + cuentaRepository.count());
    }

}
