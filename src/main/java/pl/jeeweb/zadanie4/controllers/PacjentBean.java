package pl.jeeweb.zadanie4.controllers;

import java.text.ParseException;
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
import javax.servlet.http.HttpServletRequest;
import pl.jeeweb.zadanie4.entity.Pacjent;
import pl.jeeweb.zadanie4.util.CRUDRunner;

public class PacjentBean implements java.io.Serializable {

    private Pacjent pacjent = new Pacjent();
    private List filterList;
    private List pacjentList;
    private String imie, nazwisko, pesel, birthdate;
    private float wzrostOd = 0F, wzrostDo = 0F, wagaOd = 0F, wagaDo = 0F;
    private Date urOd = null, urDo = null, dodOd = null, dodDo = null;

    public PacjentBean() {
    }

    public PacjentBean(Pacjent pacjent) {
        this.pacjent = pacjent;
    }

    public void setUrOd(Date urOd) {
        this.urOd = urOd;
    }

    public void setUrDo(Date urDo) {
        this.urDo = urDo;
    }

    public void setDodOd(Date dodOd) {
        this.dodOd = dodOd;
    }

    public void setDodDo(Date dodDo) {
        this.dodDo = dodDo;
    }

    public Date getUrOd() {
        return urOd;
    }

    public Date getUrDo() {
        return urDo;
    }

    public Date getDodOd() {
        return dodOd;
    }

    public Date getDodDo() {
        return dodDo;
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
        pesel = (String) value;
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

            birthdate = UIbirthDate.getValue().toString().substring(26, 28);
            pesel = UIpesel.getValue().toString().substring(0, 2);

            if (!birthdate.equals(pesel)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(form.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Data urodzenia jest niezgodna z numerem PESEL.", null));
                context.renderResponse();
            }
        } catch (NullPointerException exception) {
        }
    }

    private void getFilterData() throws ParseException {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        imie = request.getParameter("filterForm:imie");
        nazwisko = request.getParameter("filterForm:nazwisko");
        pesel = request.getParameter("filterForm:pesel");
        String wagaO = request.getParameter("filterForm:wagaOd");
        if (wagaO != null && !wagaO.isEmpty())
            wagaOd = Float.valueOf(wagaO);

        String wagaD = request.getParameter("filterForm:wagaDo");
        if (wagaD != null && !wagaD.isEmpty())
            wagaDo = Float.valueOf(wagaD);

        String wzrostO = request.getParameter("filterForm:wzrostOd");
        if (wzrostO != null && !wzrostO.isEmpty())
            wzrostOd = Float.valueOf(wzrostO);

        String wzrostD = request.getParameter("filterForm:wzrostDo");
        if (wzrostD != null && !wzrostD.isEmpty())
            wzrostDo = Float.valueOf(wzrostD);
    }

    public String filtruj() throws ParseException {
        getFilterData();
        filterList = CRUDRunner.filterRetrive(imie, nazwisko, pesel, urOd, urDo, wagaOd, wagaDo, wzrostOd, wzrostDo, dodOd, dodDo);
        if (filterList != null) {
            return "/pacjencifilter.xhtml";
        } else {
            return "/pacjenci.xhtml";
        }
    }

    public String dodaj(Pacjent pacjent) {
        CRUDRunner.create(pacjent);
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
