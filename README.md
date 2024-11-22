
# **Ecolab: Aplicativo para Gerenciamento de Práticas Ambientais Sustentáveis**

## **Visão Geral**

**Ecolab** é um aplicativo desenvolvido para promover práticas ambientais sustentáveis e proporcionar aos usuários uma forma fácil de gerenciar suas informações pessoais enquanto se engajam com soluções ecológicas. O app oferece funcionalidades que permitem ao usuário se informar sobre objetivos ambientais, práticas sustentáveis e atualizar suas informações de contato, tudo em uma interface simples e intuitiva.

## **Funcionalidades**

O aplicativo **Ecolab** oferece as seguintes funcionalidades:

### 1. **Tela de Login**
- Permite a autenticação segura dos usuários, garantindo o acesso exclusivo a conteúdos personalizados. O login pode ser feito por meio de credenciais específicas do usuário.

### 2. **Tela Inicial (Home)**
- A tela principal do aplicativo fornece acesso fácil às principais funcionalidades do app:
  - **Objetivo**: Exibe o propósito e a missão do app, explicando como o Ecolab pode ajudar na conscientização ambiental e promoção de práticas sustentáveis.
  - **Sobre**: Apresenta informações detalhadas sobre o aplicativo, sua criação e sua contribuição para o meio ambiente.
  - **Soluções**: Oferece uma lista de soluções práticas e dicas para adotar hábitos sustentáveis no dia a dia.

### 3. **Atualização de Dados Pessoais**
- O usuário pode atualizar suas informações pessoais, como nome e número de telefone, de forma simples e rápida. A validação dos dados é feita para garantir a precisão e a consistência das informações fornecidas.

### 4. **Navegação Rápida e Intuitiva**
- O app oferece uma navegação intuitiva entre as telas, com botões de "Home" que retornam à tela inicial, e navegação direta para as seções de Objetivo, Sobre e Soluções, garantindo uma experiência de usuário fluida e eficiente.

## **Tecnologias Utilizadas**

Este aplicativo foi desenvolvido utilizando as seguintes tecnologias:

- **Android SDK**: O app foi desenvolvido para Android, utilizando o Android Studio como ambiente de desenvolvimento integrado (IDE).
- **Firebase Authentication**: Para autenticação de usuários e gerenciamento de sessões de login.
- **Firebase Firestore**: Utilizado para armazenar e sincronizar dados de usuários em tempo real, garantindo a integridade e disponibilidade das informações.
- **Kotlin**: Linguagem de programação principal utilizada no desenvolvimento do app.

## **Instalação e Configuração**

### **Requisitos**

- **Android Studio**: A última versão do Android Studio para compilar e rodar o aplicativo.
- **Conta Firebase**: Uma conta no Firebase para configurar os serviços de autenticação e banco de dados.

### **Passos para Instalar**

1. **Clone o Repositório**

   Para começar, clone o repositório do projeto em sua máquina local:
   ```bash
   https://github.com/victorFanfoni/EcoLab.git
   ```

2. **Configuração do Firebase**
   - Crie um novo projeto no [Firebase Console](https://console.firebase.google.com/).
   - No painel do Firebase, adicione um novo aplicativo Android.
   - Baixe o arquivo `google-services.json` e adicione-o ao diretório `app/` do seu projeto Android.
   - Habilite o Firebase Authentication e o Firestore Database no Firebase Console.

3. **Abra o Projeto no Android Studio**
   - Abra o Android Studio e carregue o projeto.
   - Execute o aplicativo em um dispositivo Android ou no emulador.

4. **Compilação e Execução**
   - Após configurar o Firebase e o Android Studio, compile o app e execute-o em um dispositivo de sua escolha.

## **Contribuindo para o Projeto**

Contribuições são bem-vindas! Para contribuir com o projeto, siga os passos abaixo:

1. **Faça um Fork** deste repositório.
2. Crie uma **nova branch** para a sua funcionalidade ou correção de bug.
3. **Realize suas alterações** e escreva testes para garantir que o código está funcionando como esperado.
4. Envie um **pull request** detalhando as alterações realizadas e os motivos para elas.

## **Licença**

Este projeto está licenciado sob a **MIT License**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## **Notas Finais**

O **Ecolab** está em constante desenvolvimento, e novas funcionalidades serão adicionadas em versões futuras. Fique atento para atualizações e melhorias contínuas, visando sempre proporcionar uma experiência melhor e mais impactante no engajamento com práticas ambientais sustentáveis.
