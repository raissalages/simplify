function enviarCadastro() {
    // Obtenha os valores dos campos do formulário
    var nome = document.getElementById('nomeCompleto').value;
    var email = document.getElementById('email').value;
    var senha = document.getElementById('senha').value;
    var dataNascimento = document.getElementById('dataNascimento').value;
    var genero = document.getElementById('genero').value;

    // Crie um objeto com os dados do usuário
    var usuario = {
        nomeCompleto: nome,
        senha: senha,
        email: email,
        genero: genero,
        dataNascimento: dataNascimento,
        despesas: []
    };

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
           if (response.status === 200) {
              // Login bem-sucedido
              window.location.href = 'paginainicial.html';
          } else if (response.status === 401) {
              // Credenciais inválidas
              alert('Tente novamente.');
       }})
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
               if (data.token) {
                          // Armazenar o token no localStorage
                          localStorage.setItem('token', data.token);

                          // Redirecionar para a página principal
                          window.location.href = 'paginainicial.html';
                          }
           } else if (response.status === 401) {
               // Credenciais inválidas
               alert('Credenciais incorretas. Tente novamente.');
           } else {
               // Outro erro
               alert('Erro ao verificar credenciais. Tente novamente.');
           }
       })
       .catch(error => {
           console.error('Erro ao verificar credenciais:', error);
           alert('Erro ao verificar credenciais. Tente novamente.');
       });
}

function obterToken() {
    return localStorage.getItem('token');
}

// Exemplo de como incluir o token nas solicitações futuras
function fazerSolicitacaoProtegida() {
    const token = obterToken();

    fetch('localhost:8080/paginainicial.html', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    })
    .then(response => {
        // Processar a resposta do servidor
    })
    .catch(error => {
        console.error('Erro na solicitação protegida:', error);
        // Lidar com erros
    });
}