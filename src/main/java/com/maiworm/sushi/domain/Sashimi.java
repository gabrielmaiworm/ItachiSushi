package com.maiworm.sushi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sashimi.
 */
@Entity
@Table(name = "sashimi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sashimi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Lob
    @Column(name = "imagem")
    private byte[] imagem;

    @Column(name = "imagem_content_type")
    private String imagemContentType;

    @Column(name = "preco")
    private Double preco;

    @Column(name = "promocao")
    private Boolean promocao;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "especiais", "entradas", "sushis", "sashimis", "makimonos", "hots", "harumakis", "temakis", "yakisobas", "uramakis" },
        allowSetters = true
    )
    private Cardapio cardapio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sashimi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Sashimi nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Sashimi descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getImagem() {
        return this.imagem;
    }

    public Sashimi imagem(byte[] imagem) {
        this.setImagem(imagem);
        return this;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImagemContentType() {
        return this.imagemContentType;
    }

    public Sashimi imagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
        return this;
    }

    public void setImagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
    }

    public Double getPreco() {
        return this.preco;
    }

    public Sashimi preco(Double preco) {
        this.setPreco(preco);
        return this;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Boolean getPromocao() {
        return this.promocao;
    }

    public Sashimi promocao(Boolean promocao) {
        this.setPromocao(promocao);
        return this;
    }

    public void setPromocao(Boolean promocao) {
        this.promocao = promocao;
    }

    public Cardapio getCardapio() {
        return this.cardapio;
    }

    public void setCardapio(Cardapio cardapio) {
        this.cardapio = cardapio;
    }

    public Sashimi cardapio(Cardapio cardapio) {
        this.setCardapio(cardapio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sashimi)) {
            return false;
        }
        return id != null && id.equals(((Sashimi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sashimi{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", imagem='" + getImagem() + "'" +
            ", imagemContentType='" + getImagemContentType() + "'" +
            ", preco=" + getPreco() +
            ", promocao='" + getPromocao() + "'" +
            "}";
    }
}
