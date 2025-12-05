package MOTOAPPAPI.MotoAppApi.controller;

import MOTOAPPAPI.MotoAppApi.model.Registro;
import MOTOAPPAPI.MotoAppApi.service.RegistroService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final RegistroService registroService;

    public DashboardController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // 1. ESTATÍSTICAS GERAIS (Cards do topo) - CORRIGIDO
    @GetMapping("/estatisticas/{usuarioId}")
    public Map<String, Object> getEstatisticasDashboard(@PathVariable UUID usuarioId) {
        List<Registro> registros = registroService.listarPorUsuario(usuarioId);

        int totalCorridas = registros.stream().mapToInt(Registro::getCorridasRealizadas).sum();
        BigDecimal lucroTotal = registros.stream()
                .map(Registro::getLucro)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gastosTotais = registros.stream()
                .map(r -> {
                    BigDecimal total = BigDecimal.ZERO;
                    if (r.getCombustivel() != null) total = total.add(r.getCombustivel());
                    if (r.getAlimentacao() != null) total = total.add(r.getAlimentacao());
                    if (r.getGastosAdicionais() != null) total = total.add(r.getGastosAdicionais());
                    return total;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int kmRodados = registros.stream().mapToInt(Registro::getKmRodados).sum();

        // CORREÇÃO: Usar HashMap explicitamente
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalCorridas", totalCorridas);
        resultado.put("lucroTotal", "R$" + lucroTotal);
        resultado.put("gastosTotais", "R$" + gastosTotais);
        resultado.put("kmRodados", kmRodados + " km");

        return resultado;
    }

    // 2. CORRIDAS POR DIA (Gráfico de barras) - CORRIGIDO
    @GetMapping("/corridas-por-dia/{usuarioId}")
    public List<Map<String, Object>> getCorridasPorDia(@PathVariable UUID usuarioId,
                                                       @RequestParam String inicio,
                                                       @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Registro> registros = registroService.listarPorData(usuarioId, dataInicio, dataFim);

        // CORREÇÃO: Usar HashMap explicitamente
        return registros.stream()
                .map(registro -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("data", registro.getData().toString());
                    item.put("corridas", registro.getCorridasRealizadas());
                    item.put("plataforma", registro.getPlataforma());
                    return item;
                })
                .collect(Collectors.toList());
    }

    // 3. LUCRO vs GASTOS (Gráfico de linhas) - CORRIGIDO
    @GetMapping("/lucro-gastos/{usuarioId}")
    public List<Map<String, Object>> getLucroVsGastos(@PathVariable UUID usuarioId,
                                                      @RequestParam String inicio,
                                                      @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Registro> registros = registroService.listarPorData(usuarioId, dataInicio, dataFim);

        // CORREÇÃO: Usar HashMap explicitamente
        return registros.stream()
                .map(registro -> {
                    BigDecimal gastos = BigDecimal.ZERO;
                    if (registro.getCombustivel() != null) gastos = gastos.add(registro.getCombustivel());
                    if (registro.getAlimentacao() != null) gastos = gastos.add(registro.getAlimentacao());
                    if (registro.getGastosAdicionais() != null) gastos = gastos.add(registro.getGastosAdicionais());

                    Map<String, Object> item = new HashMap<>();
                    item.put("data", registro.getData().toString());
                    item.put("lucro", registro.getLucro());
                    item.put("gastos", gastos);
                    item.put("valorBruto", registro.getValorBruto());
                    return item;
                })
                .collect(Collectors.toList());
    }

    // 4. ESTATÍSTICAS POR PLATAFORMA - CORRIGIDO
    @GetMapping("/plataformas/{usuarioId}")
    public Map<String, Object> getEstatisticasPorPlataforma(@PathVariable UUID usuarioId) {
        List<Registro> registros = registroService.listarPorUsuario(usuarioId);

        Map<String, Long> corridasPorPlataforma = registros.stream()
                .collect(Collectors.groupingBy(Registro::getPlataforma,
                        Collectors.summingLong(Registro::getCorridasRealizadas)));

        Map<String, BigDecimal> lucroPorPlataforma = registros.stream()
                .collect(Collectors.groupingBy(Registro::getPlataforma,
                        Collectors.reducing(BigDecimal.ZERO, Registro::getLucro, BigDecimal::add)));

        // CORREÇÃO: Usar HashMap explicitamente
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("corridasPorPlataforma", corridasPorPlataforma);
        resultado.put("lucroPorPlataforma", lucroPorPlataforma);

        return resultado;
    }

    // 5. ÚLTIMOS REGISTROS - CORRIGIDO
    @GetMapping("/ultimos-registros/{usuarioId}")
    public List<Registro> getUltimosRegistros(@PathVariable UUID usuarioId,
                                              @RequestParam(defaultValue = "5") int limite) {
        List<Registro> registros = registroService.listarPorUsuario(usuarioId);

        return registros.stream()
                .sorted((r1, r2) -> r2.getData().compareTo(r1.getData()))
                .limit(limite)
                .collect(Collectors.toList());
    }
}