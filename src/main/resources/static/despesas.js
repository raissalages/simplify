let ordenacaoAscendente = true;

function formatarData(dataString) {
	const data = new Date(dataString);
	const dia = String(data.getDate()).padStart(2, '0');
	const mes = String(data.getMonth() + 1).padStart(2, '0');
	const ano = data.getFullYear();

	const dataFormatada = `${dia}/${mes}/${ano}`;

	return dataFormatada; // dd/mm/aaaa
}

function formatarDataDisplay(dataString) {
	const data = new Date(dataString);
	const ano = data.getFullYear();
	const mes = String(data.getMonth() + 1).padStart(2, '0');
	const dia = String(data.getDate()).padStart(2, '0');

	const dataFormatada = `${ano}-${mes}-${dia}`;

	return dataFormatada; // aaaa-mm-dd
}

function formatarDataTimestamp(dataString) {
    const data = new Date(dataString);
    const ano = data.getFullYear();
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const dia = String(data.getDate()).padStart(2, '0');

    // Adiciona a parte do tempo à formatação
    const dataFormatada = `${ano}-${mes}-${dia}T00:00:00.000+00:00`;

    return dataFormatada; // yyyy-mm-ddThh:mm:ss.sssZ
}
function carregarCategorias() {
	const selectCategoria = document.getElementById('categoria');

	// Limpa as opções existentes
	selectCategoria.innerHTML = '<option value="">Categorias</option>';

	// Busca as categorias no backend
	fetch('http://localhost:8080/categoria/listar-categorias')
		.then(response => response.json())
		.then(categorias => {
			categorias.forEach(categoria => {
				const option = document.createElement('option');
				//option.value = categoria.id;
				option.textContent = categoria.nome;
				selectCategoria.appendChild(option);
			});
		})
		.catch(error => console.error('Erro ao buscar categorias:', error));
}

function obterNomeCategoria(id) {
	return fetch(`http://localhost:8080/categoria/${id}`)
		.then(response => response.json())
		.then(categoria => categoria.nome)
		.catch(error => {
			console.error(`Erro ao obter categoria com ID ${id}:`, error);
			return 'Categoria desconhecida';
		});
}

function carregarCategoriasPopUp() {


	const selectCategoria = document.getElementById('categoriaPopUp');

	// Limpa as opções existentes
	selectCategoria.innerHTML = '<option value="">Categorias</option>';

	// Busca as categorias no backend
	fetch('http://localhost:8080/categoria/listar-categorias')
		.then(response => response.json())
		.then(categorias => {
			categorias.forEach(categoria => {
				const option = document.createElement('option');
				option.value = categoria.id;
				option.textContent = categoria.nome;
				selectCategoria.appendChild(option);
			});
		})
		.catch(error => console.error('Erro ao buscar categorias:', error));
}


// Função para buscar e exibir despesas
function carregarDespesas() {
	fetch('http://localhost:8080/despesa/despesas')
		.then(response => response.json())
		.then(despesas => {
			const listaDespesas = document.getElementById('listaDespesas');

			despesas.forEach(despesa => {
				const itemLista = document.createElement('tr');
				itemLista.id = `despesa-${despesa.id}`;  // Adicione o ID ao elemento <tr>
				const dataFormatada = formatarData(despesa.data);
				obterNomeCategoria(despesa.categoria)
					.then(nomeCategoria => {
						// Preencher a linha da tabela com os detalhes da despesa
						itemLista.innerHTML = `
						<td>${despesa.descricao}</td>
						<td>${despesa.valor}</td>
						<td>${dataFormatada}</td>
						<td>${nomeCategoria}</td>
						<td>
							<button onclick="editarDespesa(${despesa.id})">Editar</button>
							<button onclick="excluirDespesa(${despesa.id})">Excluir</button>
						</td>
					`;

						listaDespesas.appendChild(itemLista);
					});
			});
			aplicarFiltros();
		})
		.catch(error => console.error('Erro ao buscar despesas:', error));
}

function ordenarTabela(coluna) {
	const tabela = document.getElementById('tabelaDespesas');
	const linhas = Array.from(tabela.getElementsByTagName('tr'));
	const cabecalho = linhas.shift();

	linhas.sort((a, b) => {
		const valorA = a.getElementsByTagName('td')[coluna].textContent.trim();
		const valorB = b.getElementsByTagName('td')[coluna].textContent.trim();

		return ordenacaoAscendente ? valorA.localeCompare(valorB) : valorB.localeCompare(valorA);
	});

	// Adiciona novamente o cabeçalho à tabela
	tabela.innerHTML = '';
	tabela.appendChild(cabecalho);
	linhas.forEach(linha => tabela.appendChild(linha));

	// Inverte a ordem de classificação para a próxima vez
	ordenacaoAscendente = !ordenacaoAscendente;
}

function aplicarFiltros() {
	const filtroNome = document.getElementById('nome').value.toLowerCase();
	const filtroCategoria = document.getElementById('categoria').value;
	const filtroValorMin = parseFloat(document.getElementById('valorMin').value) || 0;
	const filtroValorMax = parseFloat(document.getElementById('valorMax').value) || Number.MAX_VALUE;



	const listaDespesas = document.getElementById('listaDespesas');
	const linhas = Array.from(listaDespesas.getElementsByTagName('tr'));

	linhas.forEach(linha => {
		const colunas = linha.getElementsByTagName('td');

		const nome = colunas[0].textContent.toLowerCase();
		const valor = parseFloat(colunas[1].textContent);
		const periodo = colunas[2].textContent;
		const idCategoria = colunas[3].textContent;



		const atendeFiltros =
			(filtroNome === '' || nome.includes(filtroNome)) &&
			(valor >= filtroValorMin && valor <= filtroValorMax) &&
			(filtroCategoria === '' || idCategoria === filtroCategoria);

		linha.style.display = atendeFiltros ? '' : 'none';
	});
}

function limparFiltros() {
	document.getElementById('nome').value = '';
	document.getElementById('valorMin').value = '';
	document.getElementById('valorMax').value = '';
	document.getElementById('categoria').value = '';


	// Restaura a exibição de todas as linhas
	const listaDespesas = document.getElementById('listaDespesas');
	const linhas = Array.from(listaDespesas.getElementsByTagName('tr'));
	linhas.forEach(linha => linha.style.display = '');
}

function excluirDespesa(id) {
	const itemLista = document.getElementById(`despesa-${id}`);
	itemLista.remove();

	fetch(`http://localhost:8080/despesa/${id}`, {
		method: 'DELETE',
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`Erro ao excluir despesa: ${response.status}`);
			}
			console.log('Despesa excluída com sucesso!');
		})
		.catch(error => console.error('Erro ao excluir despesa:', error));
}
// Chame a função ao carregar a página



async function editarDespesa(id) {
	try {
		const response = await fetch(`http://localhost:8080/despesa/${id}`);
		const despesa = await response.json();
		exibirPopUpEdicao(despesa);
	} catch (error) {
		console.error('Erro ao obter detalhes da despesa:', error);
	}
}

function carregarCategoriasNoPopUp() {

    // Limpa as opções existentes
    categoriaPopUp.innerHTML = '<option value="">Categorias</option>';

    // Busca as categorias no backend
    fetch('http://localhost:8080/categoria/listar-categorias')
        .then(response => response.json())
        .then(categorias => {
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.id;  // Use o ID da categoria
                option.textContent = categoria.nome;
                categoriaPopUp.appendChild(option);
            });
        })
        .catch(error => console.error('Erro ao buscar categorias:', error));
}

function exibirPopUpEdicao(despesa) {

    //carregarCategoriasNoPopUp();
	console.log(despesa.categoria);

	const popUpConteudo = `
	<h2>Editar Despesa</h2>
	<span class="pop-up-close" onclick="fecharPopUp()">&times;</span>

	<label for="descricao">Descrição:</label>
	<input type="text" id="descricao" value="${despesa.descricao}" required>

	<label for="valor">Valor:</label>
	<input type="number" id="valor" value="${despesa.valor}" step="0.01" required>

	<label for="data">Data:</label>
	<input type="date" id="data" value="${formatarDataDisplay(despesa.data)}" required>

	<label for="categoriaPopUp">Categoria:</label>
	<select id="categoriaPopUp" required>
		<!-- Opções de categoria serão preenchidas dinamicamente do backend -->
	</select>

	<button onclick="atualizarDespesa(${despesa.id})">Atualizar Despesa</button>
`;

	// Crie um elemento div para o pop-up
	const popUpDiv = document.createElement('div');
	popUpDiv.classList.add('pop-up');
	popUpDiv.innerHTML = popUpConteudo;

	// Adicione o pop-up ao corpo do documento
	document.body.appendChild(popUpDiv);

	// Carregue as categorias para preencher o dropdown no pop-up
	//obterNomeCategoria(despesa.categoria) // Correção: use despesa.categoria.id
    //carregarCategoriasNoPopUp();
    carregarCategoriasPopUp();

    const categoriaDropdown = document.getElementById('categoriaPopUp');
    categoriaDropdown.value = despesa.categoria;
    console.log(despesa);
    console.log(despesa.categoria);

}

function atualizarDespesa(id) {
	const descricao = document.getElementById('descricao').value;
	const valor = parseFloat(document.getElementById('valor').value);
	const dataString = document.getElementById('data').value;
	const categoria = document.getElementById('categoriaPopUp').value;

    //const dataFormatada = formatarDataTimestamp(dataString);

    const dataFormatada = new Date(dataString).toISOString();
    console.log(descricao);
    console.log(dataFormatada);



	// Construa o objeto de despesa atualizado
	const despesaAtualizada = {
		descricao: descricao,
		valor: valor,
		data: dataFormatada,
		categoria: categoria
	};

    console.log(despesaAtualizada);

	// Envie a despesa atualizada para o servidor usando o método PUT
	fetch(`http://localhost:8080/despesa/${id}`, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(despesaAtualizada),
	})
		.then(response => {
			if (response.ok) {
				alert('Despesa atualizada com sucesso!');
				// Feche o pop-up após a atualização bem-sucedida
				fecharPopUp();
			} else {
				alert('Erro ao atualizar despesa.');
			}
		})
		.catch(error => console.error('Erro na requisição de atualização:', error));
}

function fecharPopUp() {
	// Remova o pop-up do corpo do documento
	const popUpDiv = document.querySelector('.pop-up');
	if (popUpDiv) {
		document.body.removeChild(popUpDiv);
	}
}


carregarCategorias();

carregarDespesas();
