function enviarCadastro() {
	var nome = document.getElementById('nomeCompleto').value;
	var email = document.getElementById('email').value;
	var senha = document.getElementById('senha').value;
	var dataNascimento = document.getElementById('dataNascimento').value;
	var genero = document.getElementById('genero').value;

	if (!(nome && email && senha && dataNascimento && genero)) {
		alert('Preencha todos os campos.')
	} else {
		var usuario = {
			nomeCompleto: nome,
			senha: senha,
			email: email,
			genero: genero,
			dataNascimento: dataNascimento,
			categorias: [],
			despesas: []
		};
	}


	// Realize uma requisição POST para o endpoint do backend
	fetch('http://localhost:8080/usuario/cadastrar', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(usuario)
	})
		.then(response => {
			// Verifique se a resposta indica sucesso (status 2xx)
			if (response.status === 200 || response.status === 201) {
				// Deu certo
				window.location.href = 'paginainicial.html';
			} else if (response.status === 400 || response.status === 401) {
				// Não deu
				alert('Tente novamente.');
			}
		})
		.catch(error => {
		});
}

function realizarLogin() {
	var email = document.getElementById('email').value;
	var senha = document.getElementById('senha').value;

	var credenciais = {
		email: email,
		senha: senha
	};

	fetch('http://localhost:8080/usuario/login', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(credenciais)
	})
		.then(response => {
			if (response.status === 200) {
				console.log(response.status);
				// Armazenar o token no localStorage
				//localStorage.setItem('token', data.token);

				// Redirecionar para a página principal
				return response.text();


			} else if (response.status === 401) {
				// Credenciais inválidas
				alert('Credenciais incorretas. Tente novamente.');
			} else {
				alert('Erro ao verificar credenciais. Tente novamente.');
			}
		})
		.then(sessionId => {
			// Armazenar o ID da sessão no localStorage
			localStorage.setItem('sessionId', sessionId);
            alert(sessionId)
			// Redirecionar para a página principal
			window.location.href = '/paginainicial';
		})
		.catch(error => {
			console.error('Erro ao verificar credenciais:', error);
			alert('Erro ao verificar credenciais. Tente novamente.');
		});
}

function autenticarUsuario(email, senha) {
	return fetch('http://localhost:8080/usuario/login', {
		method: "POST",
	})
		.then(response => response.text())
		.then(sessionId => {
			localStorage.setItem("sessionId", sessionId);
			console.log("ID da Sessão:", sessionId);
			return sessionId;
		});
}

// Função para cadastrar uma nova despesa
function cadastrarDespesa(data, sessionId) {


	if (!sessionId) {
		console.error("ID da sessão não encontrado. Usuário não autenticado.");
		return;
	}


	// Exemplo de uso:
	const emailUsuario = "seuemail@example.com";
	const senhaUsuario = "suasenha";

	autenticarUsuario(emailUsuario, senhaUsuario)
		.then(sessionId => {
			// Use o ID da sessão para cadastrar uma nova despesa
			const dadosDespesa = { /*seus dados de despesa */ };
			cadastrarDespesa(dadosDespesa, sessionId);
		})
		.catch(error => console.error("Erro:", error));
}

// Função para atualizar a lista de categorias
function atualizarListaCategorias() {
	const categoriaDropdown = document.getElementById("categoria");

	// Chamada fetch para obter categorias do servidor
	fetch("/listar-categorias")
		.then(response => response.json())
		.then(categorias => {
			// Limpar as opções existentes
			categoriaDropdown.innerHTML = "";

			// Adicionar as novas opções ao dropdown
			categorias.forEach(categoria => {
				const option = document.createElement("option");
				option.value = categoria;
				option.text = categoria;
				categoriaDropdown.appendChild(option);
			});
		})
		.catch(error => console.error("Erro ao obter categorias:", error));
}
