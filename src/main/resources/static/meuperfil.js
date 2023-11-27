var senha;
var id;

async function loadUserProfile() {
    try {
      const response = await fetch('http://localhost:8080/usuario/perfil');
      const userData = await response.json();
      senha = userData.senha; // Armazene a senha globalmente
      id = userData.id;
      console.log(userData.senha);
      // Atualize os campos com os dados do usuário
      document.getElementById('name').value = userData.nomeCompleto;
      document.getElementById('email').value = userData.email;
      document.getElementById('gender').value = userData.genero;
      document.getElementById('birthdate').value = userData.dataNascimento.slice(0, 10); // Formato yyyy-mm-dd

    } catch (error) {
      console.error('Erro ao carregar o perfil do usuário:', error);
    }
};


loadUserProfile();

function editProfile() {
  document.querySelectorAll('input[readonly]').forEach(input => input.removeAttribute('readonly'));
  document.getElementById('saveButton').style.display = 'block';
}

 function saveProfileChanges() {
    // Obter os valores dos campos do formulário
     const nomeCompleto = document.getElementById('name').value;
     const email = document.getElementById('email').value; // Adicione essa linha
     const genero = document.getElementById('gender').value;
     const dataNascimento = document.getElementById('birthdate').value;
 // Construir o objeto com os dados a serem enviados
    const userData = {};

    // Adicionar campos ao objeto apenas se eles foram alterados
    if (genero && genero !== userData.genero) {
      userData.genero = genero;
    }

    if (dataNascimento && dataNascimento !== userData.dataNascimento) {
      userData.dataNascimento = dataNascimento;
    }

    if (nomeCompleto && nomeCompleto !== userData.nomeCompleto) {
      userData.nomeCompleto = nomeCompleto;
    }

    if (email && email !== userData.email) {
      userData.email = email;
    }

    console.log(userData);

    // Enviar a solicitação PATCH usando a API fetch
    fetch(`http://localhost:8080/usuario/salvar/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
        // Adicione outros cabeçalhos conforme necessário, por exemplo, token de autenticação
      },
      body: JSON.stringify(userData)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error(`Erro no pedido: ${response.status} - ${response.statusText}`);
      }
      // Lógica adicional após a atualização bem-sucedida (por exemplo, exibir uma mensagem)
      alert('Alterações no perfil salvas com sucesso!');
    })
    .catch(error => {
      console.error('Erro ao salvar alterações:', error);
      // Lógica adicional para lidar com erros, se necessário
      alert('Erro ao salvar alterações. Por favor, tente novamente.');
    });

  }
