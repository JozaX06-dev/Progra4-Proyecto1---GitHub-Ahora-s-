package una.progra4proyecto1.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oferente")
public class Oferente {
    @Id
    @Column(name = "usuario_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Size(max = 45)
    @NotNull
    @Column(name = "identificacion", nullable = false, length = 45)
    private String identificacion;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @NotNull
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nacionalidad_iso", nullable = false)
    private Nacionalidad nacionalidadIso;

    @Size(max = 45)
    @NotNull
    @Column(name = "telefono", nullable = false, length = 45)
    private String telefono;

    @Size(max = 250)
    @NotNull
    @Column(name = "lugar_residencia", nullable = false, length = 250)
    private String lugarResidencia;

    @Column (name = "cv")
    private String cv;
}