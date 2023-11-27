function cadastrarCategoria() {
    const nome = document.getElementById('nome').value;
    const limite = parseFloat(document.getElementById('limite').value);

    if (!nome || isNaN(limite)) {
        alert("Preencha corretamente todos os campos.");
        return;
    }

    // Recupera o ID do usuário da sessão (substitua pelo seu método real de obtenção do ID do usuário)

    // Cria o objeto de categoria
    const categoria = {
        nome: nome,
        limite: limite,
        valorTotalMensal: 0.0,
        despesas: []
    };

    // Simula o envio do JSON para o servidor (substitua pelo seu método real de envio)
    enviarCategoriaParaServidor(categoria);
}

function obterIdUsuarioDaSessao() {
    // Substitua esta função pela lógica real de obtenção do ID do usuário da sessão
    // Pode ser armazenado no localStorage, sessionStorage, cookie, etc.
    return 32;
}

function enviarCategoriaParaServidor(categoria) {
    // Substitua este trecho pelo código real para enviar a categoria para o servidor
    // Pode usar fetch, XMLHttpRequest ou outra biblioteca de requisições HTTP
    fetch('http://localhost:8080/categoria', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(categoria),
    })
    .then(response => {
        if (response.ok) {
            alert('Categoria cadastrada com sucesso!');
        } else {
            alert('Erro ao cadastrar categoria.');
        }
    })
    .catch(error => console.error('Erro na requisição:', error));
}

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
