package com.maiworm.sushi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cardapio.
 */
@Entity
@Table(name = "cardapio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cardapio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Especiais> especiais = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Entrada> entradas = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Sushi> sushis = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Sashimi> sashimis = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Makimono> makimonos = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Hot> hots = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Harumaki> harumakis = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Temaki> temakis = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Yakisoba> yakisobas = new HashSet<>();

    @OneToMany(mappedBy = "cardapio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<Uramaki> uramakis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cardapio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Cardapio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Especiais> getEspeciais() {
        return this.especiais;
    }

    public void setEspeciais(Set<Especiais> especiais) {
        if (this.especiais != null) {
            this.especiais.forEach(i -> i.setCardapio(null));
        }
        if (especiais != null) {
            especiais.forEach(i -> i.setCardapio(this));
        }
        this.especiais = especiais;
    }

    public Cardapio especiais(Set<Especiais> especiais) {
        this.setEspeciais(especiais);
        return this;
    }

    public Cardapio addEspeciais(Especiais especiais) {
        this.especiais.add(especiais);
        especiais.setCardapio(this);
        return this;
    }

    public Cardapio removeEspeciais(Especiais especiais) {
        this.especiais.remove(especiais);
        especiais.setCardapio(null);
        return this;
    }

    public Set<Entrada> getEntradas() {
        return this.entradas;
    }

    public void setEntradas(Set<Entrada> entradas) {
        if (this.entradas != null) {
            this.entradas.forEach(i -> i.setCardapio(null));
        }
        if (entradas != null) {
            entradas.forEach(i -> i.setCardapio(this));
        }
        this.entradas = entradas;
    }

    public Cardapio entradas(Set<Entrada> entradas) {
        this.setEntradas(entradas);
        return this;
    }

    public Cardapio addEntrada(Entrada entrada) {
        this.entradas.add(entrada);
        entrada.setCardapio(this);
        return this;
    }

    public Cardapio removeEntrada(Entrada entrada) {
        this.entradas.remove(entrada);
        entrada.setCardapio(null);
        return this;
    }

    public Set<Sushi> getSushis() {
        return this.sushis;
    }

    public void setSushis(Set<Sushi> sushis) {
        if (this.sushis != null) {
            this.sushis.forEach(i -> i.setCardapio(null));
        }
        if (sushis != null) {
            sushis.forEach(i -> i.setCardapio(this));
        }
        this.sushis = sushis;
    }

    public Cardapio sushis(Set<Sushi> sushis) {
        this.setSushis(sushis);
        return this;
    }

    public Cardapio addSushi(Sushi sushi) {
        this.sushis.add(sushi);
        sushi.setCardapio(this);
        return this;
    }

    public Cardapio removeSushi(Sushi sushi) {
        this.sushis.remove(sushi);
        sushi.setCardapio(null);
        return this;
    }

    public Set<Sashimi> getSashimis() {
        return this.sashimis;
    }

    public void setSashimis(Set<Sashimi> sashimis) {
        if (this.sashimis != null) {
            this.sashimis.forEach(i -> i.setCardapio(null));
        }
        if (sashimis != null) {
            sashimis.forEach(i -> i.setCardapio(this));
        }
        this.sashimis = sashimis;
    }

    public Cardapio sashimis(Set<Sashimi> sashimis) {
        this.setSashimis(sashimis);
        return this;
    }

    public Cardapio addSashimi(Sashimi sashimi) {
        this.sashimis.add(sashimi);
        sashimi.setCardapio(this);
        return this;
    }

    public Cardapio removeSashimi(Sashimi sashimi) {
        this.sashimis.remove(sashimi);
        sashimi.setCardapio(null);
        return this;
    }

    public Set<Makimono> getMakimonos() {
        return this.makimonos;
    }

    public void setMakimonos(Set<Makimono> makimonos) {
        if (this.makimonos != null) {
            this.makimonos.forEach(i -> i.setCardapio(null));
        }
        if (makimonos != null) {
            makimonos.forEach(i -> i.setCardapio(this));
        }
        this.makimonos = makimonos;
    }

    public Cardapio makimonos(Set<Makimono> makimonos) {
        this.setMakimonos(makimonos);
        return this;
    }

    public Cardapio addMakimono(Makimono makimono) {
        this.makimonos.add(makimono);
        makimono.setCardapio(this);
        return this;
    }

    public Cardapio removeMakimono(Makimono makimono) {
        this.makimonos.remove(makimono);
        makimono.setCardapio(null);
        return this;
    }

    public Set<Hot> getHots() {
        return this.hots;
    }

    public void setHots(Set<Hot> hots) {
        if (this.hots != null) {
            this.hots.forEach(i -> i.setCardapio(null));
        }
        if (hots != null) {
            hots.forEach(i -> i.setCardapio(this));
        }
        this.hots = hots;
    }

    public Cardapio hots(Set<Hot> hots) {
        this.setHots(hots);
        return this;
    }

    public Cardapio addHot(Hot hot) {
        this.hots.add(hot);
        hot.setCardapio(this);
        return this;
    }

    public Cardapio removeHot(Hot hot) {
        this.hots.remove(hot);
        hot.setCardapio(null);
        return this;
    }

    public Set<Harumaki> getHarumakis() {
        return this.harumakis;
    }

    public void setHarumakis(Set<Harumaki> harumakis) {
        if (this.harumakis != null) {
            this.harumakis.forEach(i -> i.setCardapio(null));
        }
        if (harumakis != null) {
            harumakis.forEach(i -> i.setCardapio(this));
        }
        this.harumakis = harumakis;
    }

    public Cardapio harumakis(Set<Harumaki> harumakis) {
        this.setHarumakis(harumakis);
        return this;
    }

    public Cardapio addHarumaki(Harumaki harumaki) {
        this.harumakis.add(harumaki);
        harumaki.setCardapio(this);
        return this;
    }

    public Cardapio removeHarumaki(Harumaki harumaki) {
        this.harumakis.remove(harumaki);
        harumaki.setCardapio(null);
        return this;
    }

    public Set<Temaki> getTemakis() {
        return this.temakis;
    }

    public void setTemakis(Set<Temaki> temakis) {
        if (this.temakis != null) {
            this.temakis.forEach(i -> i.setCardapio(null));
        }
        if (temakis != null) {
            temakis.forEach(i -> i.setCardapio(this));
        }
        this.temakis = temakis;
    }

    public Cardapio temakis(Set<Temaki> temakis) {
        this.setTemakis(temakis);
        return this;
    }

    public Cardapio addTemaki(Temaki temaki) {
        this.temakis.add(temaki);
        temaki.setCardapio(this);
        return this;
    }

    public Cardapio removeTemaki(Temaki temaki) {
        this.temakis.remove(temaki);
        temaki.setCardapio(null);
        return this;
    }

    public Set<Yakisoba> getYakisobas() {
        return this.yakisobas;
    }

    public void setYakisobas(Set<Yakisoba> yakisobas) {
        if (this.yakisobas != null) {
            this.yakisobas.forEach(i -> i.setCardapio(null));
        }
        if (yakisobas != null) {
            yakisobas.forEach(i -> i.setCardapio(this));
        }
        this.yakisobas = yakisobas;
    }

    public Cardapio yakisobas(Set<Yakisoba> yakisobas) {
        this.setYakisobas(yakisobas);
        return this;
    }

    public Cardapio addYakisoba(Yakisoba yakisoba) {
        this.yakisobas.add(yakisoba);
        yakisoba.setCardapio(this);
        return this;
    }

    public Cardapio removeYakisoba(Yakisoba yakisoba) {
        this.yakisobas.remove(yakisoba);
        yakisoba.setCardapio(null);
        return this;
    }

    public Set<Uramaki> getUramakis() {
        return this.uramakis;
    }

    public void setUramakis(Set<Uramaki> uramakis) {
        if (this.uramakis != null) {
            this.uramakis.forEach(i -> i.setCardapio(null));
        }
        if (uramakis != null) {
            uramakis.forEach(i -> i.setCardapio(this));
        }
        this.uramakis = uramakis;
    }

    public Cardapio uramakis(Set<Uramaki> uramakis) {
        this.setUramakis(uramakis);
        return this;
    }

    public Cardapio addUramaki(Uramaki uramaki) {
        this.uramakis.add(uramaki);
        uramaki.setCardapio(this);
        return this;
    }

    public Cardapio removeUramaki(Uramaki uramaki) {
        this.uramakis.remove(uramaki);
        uramaki.setCardapio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cardapio)) {
            return false;
        }
        return id != null && id.equals(((Cardapio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cardapio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
