package io.github.mhagnumdw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlar a navegação na página web com paginação em resultados de pesquisa.
 */
public class PaginationScrollStatus {

    private static final Logger log = LoggerFactory.getLogger(PaginationScrollStatus.class);

    private int totalDeRegistros = 0;
    private int lastPage = 0;
    private int page = 0;
    private int offset = 0;
    private int pageSize;

    /**
     * Inicializa um novo objeto {@code PaginationScrollStatus}.
     *
     * @param page
     *            página, se inválido assumirá zero (0)
     * @param pageSize
     *            quantidade de registros exibidos por página
     * @param totalDeRegistros
     *            quantidade total de registros
     */
    public PaginationScrollStatus(int page, int pageSize, int totalDeRegistros) {
        this.totalDeRegistros = totalDeRegistros;

        lastPage = totalDeRegistros / pageSize;
        if (totalDeRegistros % pageSize == 0 && lastPage > 0) {
            lastPage = lastPage - 1;
        }

        this.pageSize = pageSize;

        if (page < 0 || page > lastPage) {
            log.warn("page invlaido: " + page + ", setando para 0");
            page = 0;
        }

        this.page = page;

        this.offset = page * pageSize;
    }

    public Integer getPrimeiraPagina() {
        return page > 0 ? 0 : null;
    }

    public Integer getPaginaAnterior() {
        return page > 0 ? page - 1 : null;
    }

    public Integer getProximaPagina() {
        return page < lastPage ? page + 1 : null;
    }

    public Integer getUltimaPagina() {
        return page < lastPage ? lastPage : null;
    }

    public int getTotalDeRegistros() {
        return totalDeRegistros;
    }

    public int getTotalDePaginas() {
        return lastPage + 1;
    }

    public int getPaginaAtual() {
        return page + 1;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "Total Pag(s): " + getTotalDePaginas() + ", Pag Atual: " + getPaginaAtual() + " [" + getPrimeiraPagina() + ", " + getPaginaAnterior() + ", " + getProximaPagina() + ", "
                + getUltimaPagina() + "]";
    }

}
