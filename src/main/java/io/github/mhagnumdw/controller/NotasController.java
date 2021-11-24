package io.github.mhagnumdw.controller;

import java.util.stream.Collectors;

import com.google.inject.Inject;

import io.github.mhagnumdw.entity.Nota;
import io.github.mhagnumdw.service.NotaService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;

@Path("/notas")
public class NotasController extends Controller {

    @Inject
    private NotaService notaService;

    @GET
    @Produces(Produces.TEXT)
    public String all() {
        // return "hello";
        return notaService.getAll().stream().map(Nota::getName).collect(Collectors.joining("; "));
    }

}
