package controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import model.Imovel;
import model.TipoImovel;
import repository.Imoveis;
import repository.filter.ImovelFilter;

@Controller
@RequestMapping("/imoveis")
public class ImoveisController {

	@Autowired
	private Imoveis imoveis;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroImovel");

		mv.addObject(new Imovel());
		mv.addObject(TipoImovel.values());

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView adicionar(@Validated Imovel imovel, Errors erros, RedirectAttributes atributes) {
		ModelAndView mv = new ModelAndView();

		if (erros.hasErrors()) {
			mv.setViewName("CadastroImovel");
			return mv;
		}
		// salvar no banco
		this.imoveis.guardar(imovel);
		// redirecionou
		mv.setViewName("redirect:/imoveis/novo");
		// adicionou mensagem
		atributes.addFlashAttribute("mensagem", "Imóvel Cadastrado com Sucesso!");

		return mv;
	}

	@ModelAttribute
	public List<TipoImovel> tipoImoveis() {

		return Arrays.asList(TipoImovel.values());
	}

	@RequestMapping("/pesquisa")
	public ModelAndView pesquisa() {
		ModelAndView mv = new ModelAndView("PesquisaImoveis");
		mv.addObject("filtro", new ImovelFilter());
		return mv;
	}

	@RequestMapping
	public ModelAndView filtrar(@ModelAttribute("filtro") ImovelFilter filtro) {
		ModelAndView mv = new ModelAndView("PesquisaImoveis");

		List<Imovel> imoveisFiltrados = imoveis.filtrar(filtro);
		mv.addObject(imoveisFiltrados);

		return mv;
	}
}