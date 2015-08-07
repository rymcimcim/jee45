package pl.jeeweb.zadanie4.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.jeeweb.zadanie4.entity.Pacjent;
import pl.jeeweb.zadanie4.util.CRUDRunner;
import pl.jeeweb.zadanie4.util.HibernateUtil;

public class PacjentBean implements java.io.Serializable {

    private Pacjent pacjent = new Pacjent();
    private List filterList;
    private List pacjentList;

    public PacjentBean() {
    }

    public PacjentBean(Pacjent pacjent) {
        this.pacjent = pacjent;
    }

    public List getFilterList() {
        
        return filterList;
    }

    public List<Pacjent> getPacjentList() {
        if (pacjentList == null) {
            pacjentList = CRUDRunner.retrieve();
        }
        return pacjentList;
    }

    public Pacjent getPacjent() {
        return pacjent;
    }

    public void setPacjent(Pacjent pacjent) {
        this.pacjent = pacjent;
    }

    public void checkUniquePesel(FacesContext context, UIComponent component, Object value) {
        String pesel = (String) value;
        this.getPacjentList();
        List<Pacjent> pacLis = pacjentList;
        if (pacLis.size() > 0) {
            for (Pacjent p : pacLis) {
                if (p.getPesel().equalsIgnoreCase(pesel)) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Nie można dublować numerów PESEL.", null));
                }
            }
        }
    }

    public void dateValidator(FacesContext context, UIComponent component, Object value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");

        Date date = (Date) value;
        Date today = new Date();

        if (date.after(today) || dateFormat.format(date).equals(dateFormat.format(today))) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Podaj właściwą datę urodzenia.", null));
        }
    }

    public void compareBirthdatePesel(ComponentSystemEvent event) {
        try {
            UIForm form = (UIForm) event.getComponent();
            UIInput UIbirthDate = (UIInput) form.findComponent("date");
            UIInput UIpesel = (UIInput) form.findComponent("pesel");

            String birthdate = UIbirthDate.getValue().toString().substring(27, 29);
            String pesel = UIpesel.getValue().toString().substring(0, 2);

            if (!birthdate.equals(pesel)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(form.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Data urodzenia jest niezgodna z numerem PESEL.", null));
                context.renderResponse();
            }
        } catch (NullPointerException exception) {
        }
    }
    
    public String filtruj(String imie, String nazwisko, String pesel,
            Date urOd, Date urDo, int wagaOd, int wagaDo, int wzrostOd, int wzrostDo,
            Date dodOd, Date dodDo) {
        filterList = CRUDRunner.filterRetrive(imie, nazwisko, pesel, urOd, urDo, wagaOd, wagaDo, wzrostOd, wzrostDo, dodOd, dodDo);
        return "/pacjencifilter.xhtml";
    }

    public String dodaj() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            pacjent.setId(null);
            Date now = new Date();
            pacjent.setDataDod(now);
            session.save(pacjent);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return null;
    }
    
    public String odswiez() {
        pacjentList = null;
        return null;
    }

    public void buttonAction(ActionEvent actionEvent) {
        addMessage("Użytkownik zarejestrowany!");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
