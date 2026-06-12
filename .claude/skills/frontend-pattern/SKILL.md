---
name: frontend-pattern
description: Referência do padrão de código frontend (JSF + PrimeFaces 6.1 em WebContent) usado neste projeto, para escrever ou revisar páginas .xhtml seguindo as mesmas convenções
---

Esta skill descreve as convenções das páginas JSF/PrimeFaces em `WebContent`. Use-a como
checklist ao criar ou revisar arquivos `.xhtml`. Textos visíveis ao usuário (labels,
mensagens, headers) são sempre em português (pt-BR).

## 1. Estrutura geral

- `WebContent/template/template.xhtml` é o template mestre: define `<h:head>`,
  `<h:body>`, o `p:ajaxStatus`/`statusDialog` de "Processando...", `p:messages` global e
  um `<ui:insert name="content"/>` onde o conteúdo de cada página entra.
- `WebContent/index.xhtml` é a página de login — **não** usa o template (é standalone,
  com seu próprio `<html>`/`<head>`/`<body>`), pois é a única página antes da
  autenticação.
- Páginas de funcionalidade (ex: `WebContent/pages/principal.xhtml`) usam:

```xml
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:h="http://java.sun.com/jsf/html"
                xmlns:p="http://primefaces.org/ui"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:f="http://xmlns.jcp.org/jsf/core"
                template="../template/template.xhtml">

    <ui:define name="content">
        ...
    </ui:define>

</ui:composition>
```

- Namespaces sempre `h`, `p`, `ui`, `f` (nessa ordem, com esses prefixos).

## 2. Carregamento de dados da página

- Dados iniciais (ex: lista a exibir) são carregados via `f:metadata` +
  `f:viewAction`, chamando o método de listagem do `*MB` correspondente:

```xml
<f:metadata>
    <f:viewAction action="#{clienteMB.listarClientes()}"/>
</f:metadata>
```

## 3. Formulários (`h:form`)

- Cada seção/diálogo lógico tem o seu próprio `h:form` com `id` descritivo, prefixado
  por `form`: `formClientes`, `formCadastroCliente`, `formAlterarCliente`,
  `formDeletarCliente`, `formVender`, `formRelatorios`, etc.
- Mensagens de feedback do formulário: `<p:growl id="msgXxx"/>` dentro do form
  principal da seção.
- Botões de ação que precisam atualizar outros forms usam `update=":formA, :formB"`
  (com `:` para referenciar form fora do componente atual).

## 4. Listagens (`p:dataTable`)

```xml
<p:dataTable id="tabXxx" value="#{xxxMB.listaXxx}"
             var="var" emptyMessage="Nenhum Xxx encontrado" rows="20"
             paginator="true" paginatorPosition="bottom">
    <p:column headerText="Campo" width="NN%">
        <div align="center">
            <p:outputLabel value="#{var.campo}"/>
        </div>
    </p:column>
    ...
</p:dataTable>
```

- `id` prefixado com `tab`, `var="var"`, `emptyMessage` em português começando com
  "Nenhum(a) ... encontrado(a)".
- `rows="20"` (ou menor em tabelas dentro de diálogos, ex: `rows="9"`), sempre
  `paginator="true" paginatorPosition="bottom"`.
- Cada `p:column` declara `headerText` em português e `width` em `%` (as larguras de
  todas as colunas de uma tabela somam ~100%).
- Conteúdo numérico/centralizado é envolto em `<div align="center">`.
- Formatação de valores:
  - Moeda: `<f:convertNumber type="currency" currencySymbol="R$"/>`
  - Data: `<f:convertDateTime pattern="dd/MM/yyyy"/>`
- Coluna de "Ações" no fim, com `p:commandButton`s que abrem diálogos de
  alterar/deletar/etc., cada um usando `f:setPropertyActionListener` para colocar a
  linha (`#{var}`) no managed bean antes de abrir o diálogo:

```xml
<p:commandButton oncomplete="PF('dlgAlterarXxx').show();"
                 update=":formXxx, :formAlterarXxx" icon="alterar">
    <f:setPropertyActionListener value="#{var}" target="#{xxxMB.xxx}"/>
</p:commandButton>
```

## 5. Diálogos (`p:dialog`)

```xml
<p:dialog widgetVar="dlgXxx" header="Título em Português"
          height="auto" width="auto" draggable="true" resizable="false"
          modal="true">
    <h:form id="formXxx">
        <p:panelGrid columns="N" styleClass="semBorda">
            ...campos...
        </p:panelGrid>

        <p:separator/>

        <div align="center">
            <p:panelGrid columns="2" styleClass="semBorda">
                <p:commandButton value="Ação" action="#{xxxMB.metodo()}"
                                 update=":formXxx, :formListagem" icon="confirmar"/>
                <p:commandButton value="Fechar"
                                 oncomplete="PF('dlgXxx').hide();" icon="cancelar"/>
            </p:panelGrid>
        </div>
    </h:form>
</p:dialog>
```

- `widgetVar` prefixado `dlg`, `header` em português, sempre
  `height="auto" width="auto" draggable="true" resizable="false" modal="true"`.
- Abertura via `oncomplete="PF('dlgXxx').show();"` em um `p:commandButton` externo;
  fechamento via `oncomplete="PF('dlgXxx').hide();"`.
- Diálogos de confirmação (deletar/cancelar) seguem o padrão "Deseja realmente ...?"
  com botões `Sim` (ação real, `icon="confirmar"`) e `Fechar`/`Não`
  (`icon="cancelar"`).
- Layout interno em `p:panelGrid columns="N" styleClass="semBorda"`; ações finais
  geralmente dentro de `<div align="center">` com outro `panelGrid columns="2"`.

## 6. Campos de formulário

- `p:inputText` para texto, com `maxlength`/`size` definidos.
- Campos obrigatórios: `required="true" requiredMessage="Mensagem em português"`
  (ex: `requiredMessage="Insira o telefone"`).
- `p:watermark` para dica dentro do campo de busca.
- `p:calendar` para datas: `showOn="button" pattern="dd/MM/yyyy" locale="pt"`.
- `p:selectOneMenu` + `f:selectItem`/`f:selectItems` para combos, com item placeholder
  `<f:selectItem itemLabel="Selecione" itemValue=""/>`.
- Campos condicionais usam `rendered="#{...}"` e ficam dentro de um
  `p:outputPanel` com `id`, atualizado via `p:ajax update="idDoPanel"` no componente
  que controla a condição (ex: `p:selectBooleanCheckbox`).

## 7. Ícones e botões

- `p:commandButton` usa o atributo `icon` com nomes customizados do projeto (não
  classes PrimeIcons padrão): `confirmar`, `cancelar`, `buscar`, `limpar`, `novo`,
  `alterar`, `sair`, `relatorios`, `carrinho`, `sifrao`. Reutilize esses nomes para
  ações equivalentes em vez de inventar novos.
- Botões de cabeçalho/topo de página usam `styleClass="btnCabecalho"`.

## 8. Convenções gerais

- Todo texto visível (labels, headers, mensagens de erro/obrigatoriedade,
  `emptyMessage`) é em português.
- IDs de `h:form`, `p:dataTable` e `p:dialog`/`widgetVar` seguem os prefixos
  `form`/`tab`/`dlg` + nome da entidade/ação em CamelCase.
- Ao adicionar uma tela/ação nova para uma entidade que já segue o padrão da skill
  `backend-pattern` (`*MB` com `inserir`/`alterar`/`deletar`/`listar`), replique a
  estrutura de diálogo + form + dataTable + commandButtons já usada para `Cliente` em
  `WebContent/pages/principal.xhtml`.
