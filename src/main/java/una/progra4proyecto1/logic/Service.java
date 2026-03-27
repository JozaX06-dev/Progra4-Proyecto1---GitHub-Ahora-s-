package una.progra4proyecto1.logic;

import una.progra4proyecto1.data.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PuestoRepository puestoRepository;
    @Autowired
    private OferenteRepository oferenteRepository;
    @Autowired
    private NacionalidadRepository nacionalidadRepository;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;
    @Autowired
    private HabilidadRepository habilidadRepository;
    @Autowired
    private RequisitoRepository requisitoRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public Iterable<Usuario> usuarioFindAll() {
        return usuarioRepository.findAll();
    }
    public List<Puesto> find5PuestosPublicosActivos() {
        return puestoRepository.find5PuestosPublicosActivos();
    }
    public Iterable<Oferente> oferenteFindAll() {
        return oferenteRepository.findAll();
    }
    public Iterable<Nacionalidad> nacionalidadFindAll() {
        return nacionalidadRepository.findAll();
    }
    public Nacionalidad nacionalidadFindById(String iso) {
        return nacionalidadRepository.findById(iso).orElse(null);
    }
    public Caracteristica caracteristicaFindById(int id) {
        return caracteristicaRepository.findById(id).orElse(null);
    }
    public Oferente oferenteFindById(int usuarioId) {
        return oferenteRepository.findById(usuarioId).orElse(null);
    }
    public void registrarEmpresa(String nombre, String correo, String localizacion, String telefono, String descripcion){
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setActivo((byte) 0);
        usuario.setClave("");
        usuarioRepository.save(usuario);

        Empresa empresa = new Empresa();
        empresa.setUsuario(usuario);
        empresa.setNombre(nombre);
        empresa.setLocalizacion(localizacion);
        empresa.setTelefono(telefono);
        empresa.setDescripcion(descripcion);
        empresaRepository.save(empresa);
    }

    public void registrarOferente(String correo, String identificacion, String nombre, String apellido, Nacionalidad nacionalidad, String telefono, String lugarResidencia){
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setActivo((byte) 0);
        usuario.setClave("");
        usuarioRepository.save(usuario);

        Oferente oferente = new Oferente();
        oferente.setUsuario(usuario);
        oferente.setIdentificacion(identificacion);
        oferente.setNombre(nombre);
        oferente.setApellido(apellido);
        oferente.setNacionalidadIso(nacionalidad);
        oferente.setTelefono(telefono);
        oferente.setLugarResidencia(lugarResidencia);
        oferenteRepository.save(oferente);
    }
    public Usuario login (String correo, String clave){
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
            if (usuario.isPresent() && encoder.matches(clave, usuario.get().getClave()) && usuario.get().getActivo()==(byte)1) {
                return usuario.get();
            }
        return null;
    }
    public String getTipoUsuario(Integer usuarioId){
        if (adminRepository.existsById(usuarioId)) return "admin";
        if (oferenteRepository.existsById(usuarioId)) return "oferente";
        if (empresaRepository.existsById(usuarioId)) return "empresa";
        return null;
    }
    public List<Caracteristica> caracteristicasRaiz(){

        return caracteristicaRepository.findByPadreIsNull();
    }
    public List<Caracteristica> caracteristicasHijos(Caracteristica padre){
        return caracteristicaRepository.findByPadre(padre);
    }
    public List<Habilidad> habilidadesOferente(Oferente oferente){
        return habilidadRepository.findByOferente(oferente);
    }
    public Boolean caracteristicaTieneHijos(Caracteristica caracteristica){
        return caracteristicaRepository.existsByPadre(caracteristica);
    }
    public Map<Integer, Boolean> mapTieneHijos(List<Caracteristica> caracteristicas){
        Map<Integer, Boolean> map = new HashMap<>();
        for (Caracteristica caracteristica : caracteristicas) {
            map.put(caracteristica.getId(), caracteristicaRepository.existsByPadre(caracteristica));
        }
        return map;
    }
    public List<Caracteristica> caracteristicasNodosFinales(){
        List<Caracteristica> todas = (List<Caracteristica>) caracteristicaRepository.findAll();
        List<Caracteristica> finales = new ArrayList<>();
        for (Caracteristica caracteristica:todas) {
            if (!caracteristicaRepository.existsByPadre(caracteristica)) {
                finales.add(caracteristica);
            }
        }
        return finales;
    }
    public void agregarHabilidad(Oferente oferente, Caracteristica caracteristica, Integer nivel){
        Habilidad habilidad = new Habilidad();
        habilidad.setNivel(nivel);
        habilidad.setCaracteristica(caracteristica);
        habilidad.setOferente(oferente);
        habilidadRepository.save(habilidad);
    }
    public void guardarOferente(Oferente oferente){
        oferenteRepository.save(oferente);
    }
    public List<Puesto> buscarPuestosPorCaracteristicas(List<Integer> ids){
        List<Requisito> requisitos = requisitoRepository.findByCaracteristicaIdIn(ids);
        List<Puesto> puestos = new ArrayList<>();
        for (Requisito r:requisitos){
            Puesto p = r.getPuesto();
            if(p.getEsPublico()==(byte)1 && p.getActivo()==(byte)1 && !puestos.contains(p)){
                puestos.add(p);
            }
        }
        return puestos;
    }
    public List<Caracteristica> obtenerArbolCaracteristicas(){
        List<Caracteristica> resultado = new ArrayList<>();
        for(Caracteristica raiz : caracteristicasRaiz()){
            agregarRecursivo(raiz, resultado);
        }
        return resultado;
    }
    private void agregarRecursivo(Caracteristica nodo, List<Caracteristica> lista){
        lista.add(nodo);
        for(Caracteristica hijo : caracteristicasHijos(nodo)){
            agregarRecursivo(hijo, lista);
        }
    }
    public Empresa empresaFindById(int usuarioId) {
        return empresaRepository.findById(usuarioId).orElse(null);
    }

    public List<Puesto> puestosFindByEmpresa(Empresa empresa) {
        return puestoRepository.findByEmpresa(empresa);
    }

    public Puesto puestoFindById(int id) {
        return puestoRepository.findById(id).orElse(null);
    }

    public void desactivarPuesto(int puestoId) {
        Puesto puesto = puestoRepository.findById(puestoId).orElse(null);
        if (puesto != null) {
            puesto.setActivo((byte) 0);
            puestoRepository.save(puesto);
        }
    }
    public void publicarPuesto(Empresa empresa, String descripcion, Double salario,
                               int esPublico, List<Integer> caracteristicaIds, List<Integer> niveles) {
        Puesto puesto = new Puesto();
        puesto.setEmpresa(empresa);
        puesto.setDescripcion(descripcion);
        puesto.setSalario(salario);
        puesto.setEsPublico((byte) esPublico);
        puesto.setActivo((byte) 1);
        puesto.setFechaCreacion(java.time.LocalDate.now());
        puestoRepository.save(puesto);

        if (caracteristicaIds != null) {
            for (int i = 0; i < caracteristicaIds.size(); i++) {
                int nivel = niveles.get(i);
                if (nivel > 0) { // ← solo guarda si el nivel es mayor a 0
                    Requisito req = new Requisito();
                    req.setPuesto(puesto);
                    req.setCaracteristica(caracteristicaFindById(caracteristicaIds.get(i)));
                    req.setNivelDeseado(nivel);
                    requisitoRepository.save(req);
                }
            }
        }
    }
    public List<Map<String, Object>> buscarCandidatos(Puesto puesto) {
        List<Requisito> requisitos = requisitoRepository.findByPuesto(puesto);
        if (requisitos.isEmpty()) return new ArrayList<>();

        Map<Integer, Integer> vectorEmpresa = new HashMap<>();
        for (Requisito req : requisitos) {
            vectorEmpresa.put(req.getCaracteristica().getId(), req.getNivelDeseado());
        }

        double normaEmpresa = 0;
        for (int nivel : vectorEmpresa.values()) {
            normaEmpresa += nivel * nivel;
        }
        normaEmpresa = Math.sqrt(normaEmpresa);

        List<Oferente> todos = (List<Oferente>) oferenteRepository.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Oferente oferente : todos) {
            List<Habilidad> habilidades = habilidadRepository.findByOferente(oferente);

            Map<Integer, Integer> vectorOferente = new HashMap<>();
            for (Requisito req : requisitos) {
                vectorOferente.put(req.getCaracteristica().getId(), 0); // default 0
            }
            for (Habilidad hab : habilidades) {
                int id = hab.getCaracteristica().getId();
                if (vectorOferente.containsKey(id)) {
                    vectorOferente.put(id, hab.getNivel());
                }
            }

            double normaOferente = 0;
            for (int nivel : vectorOferente.values()) {
                normaOferente += nivel * nivel;
            }
            normaOferente = Math.sqrt(normaOferente);

            if (normaOferente == 0) continue;

            double productoPunto = 0;
            for (int id : vectorEmpresa.keySet()) {
                productoPunto += vectorEmpresa.get(id) * vectorOferente.getOrDefault(id, 0);
            }
            double similitud = (productoPunto / (normaEmpresa * normaOferente)) * 100;

            int cumplidos = 0;
            for (Requisito req : requisitos) {
                int nivelOferente = vectorOferente.getOrDefault(req.getCaracteristica().getId(), 0);
                if (nivelOferente >= req.getNivelDeseado()) cumplidos++;
            }
            Map<String, Object> entrada = new HashMap<>();
            entrada.put("oferente", oferente);
            entrada.put("cumplidos", cumplidos);
            entrada.put("total", requisitos.size());
            entrada.put("similitud", String.format("%.2f", similitud));
            resultado.add(entrada);
        }
        resultado.sort((a, b) -> Double.compare(
                Double.parseDouble((String) b.get("similitud")),
                Double.parseDouble((String) a.get("similitud"))
        ));

        return resultado;
    }

    public Administrador adminFindById(int usuarioId) {
        return adminRepository.findById(usuarioId).orElse(null);
    }

    public List<Empresa> empresasPendientes(){
        return empresaRepository.findByUsuarioActivo((byte) 0);
    }

    public List<Oferente> oferentesPendientes(){
        return oferenteRepository.findByUsuarioActivo((byte) 0);
    }

    public void aprobarEmpresa(int usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario != null){
            usuario.setActivo((byte) 1);
            usuarioRepository.save(usuario);
        }
    }

    public void aprobarOferente(int usuarioId){
        aprobarEmpresa(usuarioId);
    }

    public void crearCaracteristica(String nombre, Integer padreId) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        if (padreId != null) {
            c.setPadre(caracteristicaFindById(padreId));
        }
        caracteristicaRepository.save(c);
    }
    public Double obtenerTipoCambioVenta() {
        try {
            org.springframework.web.client.RestTemplate restTemplate =
                    new org.springframework.web.client.RestTemplate();
            String url = "https://api.hacienda.go.cr/indicadores/tc/dolar";
            java.util.Map response = restTemplate.getForObject(url, java.util.Map.class);
            java.util.Map venta = (java.util.Map) response.get("venta");
            return Double.parseDouble(venta.get("valor").toString());
        } catch (Exception e) {
            return 650.0;
        }
    }
    public Map<Integer, Integer> mapNiveles(List<Caracteristica> caracteristicas){
        Map<Integer, Integer> niveles = new HashMap<>();
        for(Caracteristica c : caracteristicas){
            int nivel = 0;
            Caracteristica actual = c;
            while(actual.getPadre() != null){
                nivel++;
                actual = actual.getPadre();
            }
            niveles.put(c.getId(), nivel);
        }
        return niveles;
    }
    public byte[] generarReportePDF(int anio) throws Exception {
        List<Puesto> todos = puestoRepository.findByAnio(anio);

        Map<Integer, Long> porMes = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) porMes.put(i, 0L);
        for (Puesto p : todos) {
            int mes = p.getFechaCreacion().getMonthValue();
            porMes.put(mes, porMes.get(mes) + 1);
        }

        String[] nombresMeses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        doc.add(new Paragraph("Reporte de Puestos por Mes — " + anio)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        Table tabla = new Table(new float[]{3, 2});
        tabla.setWidth(UnitValue.createPercentValue(100));

        tabla.addHeaderCell(new Cell()
                .add(new Paragraph("Mes").setBold())
                .setBackgroundColor(new DeviceRgb(10, 202, 154))
                .setFontColor(DeviceGray.BLACK));
        tabla.addHeaderCell(new Cell()
                .add(new Paragraph("Cantidad de puestos").setBold())
                .setBackgroundColor(new DeviceRgb(10, 202, 154))
                .setFontColor(DeviceGray.BLACK));

        for (int i = 1; i <= 12; i++) {
            tabla.addCell(new Cell().add(new Paragraph(nombresMeses[i - 1])));
            tabla.addCell(new Cell()
                    .add(new Paragraph(String.valueOf(porMes.get(i))))
                    .setTextAlignment(TextAlignment.CENTER));
        }

        long total = porMes.values().stream().mapToLong(Long::longValue).sum();
        tabla.addCell(new Cell()
                .add(new Paragraph("Total").setBold())
                .setBackgroundColor(new DeviceRgb(220, 220, 220)));
        tabla.addCell(new Cell()
                .add(new Paragraph(String.valueOf(total)).setBold())
                .setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(tabla);
        doc.close();

        return baos.toByteArray();
    }

    public Map<String, Long> puestosPorMes(int anio) {
        List<Puesto> todos = puestoRepository.findByAnio(anio);
        String[] nombresMeses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        Map<String, Long> porMes = new LinkedHashMap<>();
        for (String mes : nombresMeses) porMes.put(mes, 0L);
        for (Puesto p : todos) {
            int mes = p.getFechaCreacion().getMonthValue();
            String nombreMes = nombresMeses[mes - 1];
            porMes.put(nombreMes, porMes.get(nombreMes) + 1);
        }
        return porMes;
    }
}
