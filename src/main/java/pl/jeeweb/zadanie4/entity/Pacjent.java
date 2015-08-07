package pl.jeeweb.zadanie4.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="pacjent"
    ,catalog="test2"
)
public class Pacjent  implements java.io.Serializable {


     private Integer id;
     private String imie;
     private String nazwisko;
     private String pesel;
     private Date data;
     private String adres;
     private String telefon;
     private float waga;
     private float wzrost;
     private Date dataDod;

    public Pacjent() {
    }

    public Pacjent(String imie, String nazwisko, String pesel, Date data, String adres, String telefon, float waga, float wzrost, Date dataDod) {
       this.imie = imie;
       this.nazwisko = nazwisko;
       this.pesel = pesel;
       this.data = data;
       this.adres = adres;
       this.telefon = telefon;
       this.waga = waga;
       this.wzrost = wzrost;
       this.dataDod = dataDod;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="imie", nullable=false, length=45)
    public String getImie() {
        return this.imie;
    }
    
    public void setImie(String imie) {
        this.imie = imie;
    }

    
    @Column(name="nazwisko", nullable=false, length=60)
    public String getNazwisko() {
        return this.nazwisko;
    }
    
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    
    @Column(name="pesel", nullable=false, length=11)
    public String getPesel() {
        return this.pesel;
    }
    
    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="data", nullable=false, length=10)
    public Date getData() {
        return this.data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }

    
    @Column(name="adres", nullable=false, length=150)
    public String getAdres() {
        return this.adres;
    }
    
    public void setAdres(String adres) {
        this.adres = adres;
    }

    
    @Column(name="telefon", nullable=false, length=20)
    public String getTelefon() {
        return this.telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    
    @Column(name="waga", nullable=false, precision=12, scale=0)
    public float getWaga() {
        return this.waga;
    }
    
    public void setWaga(float waga) {
        this.waga = waga;
    }

    
    @Column(name="wzrost", nullable=false, precision=12, scale=0)
    public float getWzrost() {
        return this.wzrost;
    }
    
    public void setWzrost(float wzrost) {
        this.wzrost = wzrost;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="dataDod", nullable=false, length=10)
    public Date getDataDod() {
        return this.dataDod;
    }
    
    public void setDataDod(Date dataDod) {
        this.dataDod = dataDod;
    }




}


