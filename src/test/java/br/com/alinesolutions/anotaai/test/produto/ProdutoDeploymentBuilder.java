package br.com.alinesolutions.anotaai.test.produto;

import org.jboss.shrinkwrap.api.spec.WebArchive;

import br.com.alinesolutions.anotaai.service.app.ProdutoService;
import br.com.alinesolutions.anotaai.test.deployment.DeploymentBuilder;


public class ProdutoDeploymentBuilder extends DeploymentBuilder {

	@Override
	protected void addSpecificClasses(WebArchive webArchive) {
		webArchive.addClass(ProdutoService.class).addPackage(ProdutoService.class.getPackage());
	}
}
