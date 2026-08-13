## O que muda

<!-- Uma ou duas frases. O que essa mudança faz na prática? -->

Fecha a TMOB-XXX.

## Screenshot

<!--
Se a mudança aparece na tela, coloque uma imagem. Arraste o arquivo aqui que o GitHub sobe sozinho.
Se mudou algo que já existia, vale antes e depois.
Se não muda nada visual (CI, refatoração, configuração), escreva "não se aplica" e siga.
-->

## Como testar

<!-- Passo a passo para quem revisa reproduzir. Ex: abra a aba 5 Dias sem ter passado pela Clima -->

1.
2.

## Antes de pedir revisão

- [ ] Trouxe o master para o meu branch (`git merge origin/master`) e resolvi conflitos, se houve
- [ ] Compilei (`Ctrl+F9` ou `./gradlew assembleDebug`) e não há erro
- [ ] Rodei no emulador e vi a tela funcionando
- [ ] Li meu próprio diff (`git diff origin/master`) e não tem arquivo ou código sobrando
- [ ] Textos visíveis estão no `strings.xml`, não escritos no código
- [ ] Usei os componentes e tokens do design system onde já existem
- [ ] O PR faz uma coisa só (sem mudança de build ou dependência junto de feature)

## Depende de outro PR?

<!-- Se sim, diga qual. Se este branch precisa de código que ainda não está no master, o build vai falhar. -->

Não.
