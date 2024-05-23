import java.io.PrintWriter
import java.io.File

var numLinhas = -1
var numColunas = -1

var tabuleiroHumano : Array<Array<Char?>> = emptyArray()
var tabuleiroComputador : Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano : Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador : Array<Array<Char?>> = emptyArray()

fun tamanhoTabuleiroValido(numLinhas:Int, numColunas:Int):Boolean{
    return when{
        numLinhas == 4 && numColunas == 4 -> true
        numLinhas == 5 && numColunas == 5 -> true
        numLinhas == 7 && numColunas == 7 -> true
        numLinhas == 8 && numColunas == 8 -> true
        numLinhas == 10 && numColunas == 10 -> true
        else -> false
    }
}

fun processaCoordenadas(coordenadas : String , numLinhas: Int , numColunas: Int): Pair<Int,Int>?{
    val rangeLetras: CharRange = 'A'..'Z'
    val letra = rangeLetras.first + numColunas - 1
    val resultado : Pair<Int,Int>?
    when (coordenadas.length) {
        3 -> {
            if (coordenadas[0].code-48 in 1..numLinhas && coordenadas[1] == ',' &&
                    coordenadas[coordenadas.length-1] in 'A'..letra){
                resultado =  Pair(coordenadas[0].code-48,coordenadas[coordenadas.length-1].code-64)
                return resultado
            }else return null
        }
        4 -> {
            if(((coordenadas[0].code-48) * 10 + coordenadas[1].code-48) in 1..numLinhas && coordenadas[2] == ',' &&
                    coordenadas[coordenadas.length-1] in 'A'..letra) {
                resultado = Pair(((coordenadas[0].code - 48) * 10 + coordenadas[1].code - 48), coordenadas[coordenadas.length - 1].code - 64)
                return resultado
            }else return null
        }
        else -> return null
    }
}

fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 0
    val char: CharRange = 'B'..'Z'
    var legenda = "A "
    return if (numColunas == 1) {
        "A"
    } else{
        while (count < numColunas-2) {
            legenda += "| ${char.first + count} "
            count++
        }

        legenda += "| ${char.first + numColunas - 2}"
        legenda
    }
}

fun criaTerreno(numLinhas: Int , numColunas: Int): String{
    var count = 0
    var count2 = 1
    var colunas = ""
    var linhas = ""
    while (count < numColunas) {
        colunas += "| ~ "
        count++
    }
    colunas += "|"
    while (count2 - 1 < numLinhas) {
        linhas += "\n$colunas $count2"
        count2++
    }
    return "\n| ${criaLegendaHorizontal(numColunas)} |$linhas\n"
}

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int>{
    return if (numLinhas==4 && numColunas==4){
        arrayOf(2,0,0,0)
    }else if (numLinhas==5 && numColunas==5){
        arrayOf(1,1,1,0)
    }else if (numLinhas==7 && numColunas==7){
        arrayOf(2,1,1,1)
    }else if (numLinhas==8 && numColunas==8){
        arrayOf(2,2,1,1)
    }else if (numLinhas==10 && numColunas==10){
        arrayOf(3,2,1,1)
    }else emptyArray()
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>>{
    return Array(numLinhas){Array(numColunas){null}}
}

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean{
    val numLinhas = tabuleiro.size
    val numColunas = tabuleiro[0].size
    val intervalo1= 1..numLinhas
    val intervalo2= 1..numColunas

    return linha in  intervalo1 && coluna in intervalo2
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int,Int>>): Array<Pair<Int,Int>>{
    var resultado = emptyArray<Pair<Int, Int>>()
    for (num in 0 until coordenadas.size) {
        if (coordenadas[num] != Pair(0,0)) {
            resultado += coordenadas[num]
        }
    }
    return resultado
}

fun juntarCoordenadas(coordenadas1: Array<Pair<Int,Int>>, coordenadas2: Array<Pair<Int,Int>>): Array<Pair<Int,Int>>{
        val tamanhoResultado = coordenadas1.size + coordenadas2.size
        val resultado = Array(tamanhoResultado) {Pair(0, 0)}

        for (i in 0 until coordenadas1.size) {
            resultado[i] = coordenadas1[i]
        }

        for (i in 0 until coordenadas2.size) {
            resultado[i + coordenadas1.size] = coordenadas2[i]
        }

        return resultado
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int,Int>>{
    var resultado = emptyArray<Pair<Int,Int>>()
    if (dimensao == 1){
        if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size) resultado += arrayOf(Pair(linha,coluna))
    }else if (dimensao == 2){
        if (orientacao == "E"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && coluna + 1 in 1..tabuleiro[0].size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha,coluna + 1))
            }
        }else if (orientacao == "O"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && coluna - 1 in 1..tabuleiro[0].size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha,coluna - 1))
            }
        }else if (orientacao == "N"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && linha - 1 in 1..tabuleiro.size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha - 1,coluna))
            }
        }else if (orientacao == "S"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && linha + 1 in 1..tabuleiro.size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha + 1,coluna))
            }
        }
    }else if (dimensao == 3){
        if (orientacao == "E"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && coluna + 1 in 1..tabuleiro[0].size
                    && coluna + 2 in 1..tabuleiro[0].size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha,coluna + 1),Pair(linha,coluna + 2))
            }
        }else if (orientacao == "O"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && coluna - 1 in 1..tabuleiro[0].size
                    && coluna - 2 in 1..tabuleiro[0].size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha,coluna - 1),Pair(linha,coluna - 2))
            }
        }else if (orientacao == "N"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && linha - 1 in 1..tabuleiro.size
                    && linha - 2 in 1..tabuleiro.size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha - 1,coluna),Pair(linha - 2,coluna))
            }
        }else if (orientacao == "S"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && linha + 1 in 1..tabuleiro.size
                    && linha + 2 in 1..tabuleiro.size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha + 1,coluna),Pair(linha + 2,coluna))
            }
        }
    }else if (dimensao == 4){
        if (orientacao == "E"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && coluna + 1 in 1..tabuleiro[0].size &&
                    coluna + 2 in 1..tabuleiro[0].size && coluna + 3 in 1..tabuleiro[0].size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha,coluna + 1),Pair(linha,coluna + 2),Pair(linha,coluna + 3))
            }
        }else if (orientacao == "O"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && coluna - 1 in 1..tabuleiro[0].size &&
                    coluna - 2 in 1..tabuleiro[0].size && coluna - 3 in 1..tabuleiro[0].size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha,coluna - 1),Pair(linha,coluna - 2),Pair(linha,coluna - 3))
            }
        }else if (orientacao == "N"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && linha - 1 in 1..tabuleiro.size &&
                    linha - 2 in 1..tabuleiro.size && linha - 3 in 1..tabuleiro.size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha - 1,coluna),Pair(linha - 2,coluna),Pair(linha - 3,coluna))
            }
        }else if (orientacao == "S"){
            if (linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size && linha + 1 in 1..tabuleiro.size &&
                    linha + 2 in 1..tabuleiro.size && linha + 3 in 1..tabuleiro.size){
                resultado += arrayOf(Pair(linha,coluna),Pair(linha + 1,coluna),Pair(linha + 2,coluna),Pair(linha + 3,coluna))
            }
        }
    }
    return resultado
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int,Int>>{
    var coordenadas = emptyArray<Pair<Int, Int>>()
    when (dimensao) {
        1 -> coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna - 1), Pair(linha - 1, coluna), Pair(linha - 1, coluna + 1),
                Pair(linha, coluna - 1), Pair(linha, coluna + 1), Pair(linha + 1, coluna - 1), Pair(linha + 1, coluna), Pair(linha + 1, coluna + 1)))
        2-> when (orientacao){
            "N" ->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 2, coluna-1), Pair(linha - 2, coluna),
                    Pair(linha - 2, coluna + 1), Pair(linha-1, coluna-1), Pair(linha-1, coluna + 1), Pair(linha, coluna-1),
                    Pair(linha, coluna+1), Pair(linha + 1, coluna - 1), Pair(linha + 1, coluna), Pair(linha + 1, coluna + 1)))}
            "S"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha + 2, coluna-1), Pair(linha + 2, coluna),
                    Pair(linha + 2, coluna + 1), Pair(linha + 1, coluna-1), Pair(linha + 1, coluna + 1), Pair(linha, coluna-1),
                    Pair(linha, coluna+1), Pair(linha - 1, coluna - 1), Pair(linha - 1, coluna), Pair(linha - 1, coluna + 1)))}
            "E"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna-1), Pair(linha - 1, coluna),
                    Pair(linha - 1, coluna + 1), Pair(linha - 1, coluna + 2), Pair(linha, coluna-1), Pair(linha, coluna + 2),
                    Pair(linha + 1, coluna-1), Pair(linha + 1, coluna), Pair(linha + 1, coluna + 1), Pair(linha + 1, coluna + 2)))}
            "O"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna+1), Pair(linha - 1, coluna),
                    Pair(linha - 1, coluna - 1), Pair(linha - 1, coluna - 2), Pair(linha, coluna+1), Pair(linha, coluna - 2),
                    Pair(linha + 1, coluna+1), Pair(linha + 1, coluna), Pair(linha + 1, coluna - 1), Pair(linha + 1, coluna - 2)))}
        }
        3-> when (orientacao){
            "N" ->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 3, coluna-1), Pair(linha - 3, coluna),
                    Pair(linha - 3, coluna + 1), Pair(linha - 2, coluna-1), Pair(linha - 2, coluna + 1), Pair(linha-1, coluna-1),
                    Pair(linha-1, coluna + 1), Pair(linha, coluna-1), Pair(linha, coluna+1), Pair(linha + 1, coluna - 1),
                    Pair(linha + 1, coluna), Pair(linha + 1, coluna + 1)))}

            "S"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha + 3, coluna-1), Pair(linha + 3, coluna),
                    Pair(linha + 3, coluna + 1), Pair(linha + 2, coluna-1), Pair(linha + 2, coluna + 1), Pair(linha + 1, coluna-1),
                    Pair(linha + 1, coluna + 1), Pair(linha, coluna-1), Pair(linha, coluna+1), Pair(linha - 1, coluna - 1),
                    Pair(linha - 1, coluna), Pair(linha - 1, coluna + 1)))}

            "E"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna-1), Pair(linha - 1, coluna),
                    Pair(linha - 1, coluna + 1), Pair(linha - 1, coluna + 2), Pair(linha - 1, coluna + 3), Pair(linha, coluna-1),
                    Pair(linha, coluna + 3), Pair(linha + 1, coluna-1), Pair(linha + 1, coluna), Pair(linha + 1, coluna + 1),
                    Pair(linha + 1, coluna + 2), Pair(linha + 1, coluna + 3)))}

            "O"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna+1), Pair(linha - 1, coluna),
                    Pair(linha - 1, coluna - 1), Pair(linha - 1, coluna - 2), Pair(linha - 1, coluna - 3), Pair(linha, coluna+1),
                    Pair(linha, coluna - 3), Pair(linha + 1, coluna+1), Pair(linha + 1, coluna), Pair(linha + 1, coluna - 1),
                    Pair(linha + 1, coluna - 2), Pair(linha + 1, coluna - 3)))}
        }
        4-> when (orientacao){
            "N" ->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 4, coluna-1), Pair(linha - 4, coluna),
                    Pair(linha - 4, coluna + 1), Pair(linha - 3, coluna-1), Pair(linha - 3, coluna + 1), Pair(linha - 2, coluna-1),
                    Pair(linha - 2, coluna + 1), Pair(linha-1, coluna-1), Pair(linha-1, coluna + 1), Pair(linha, coluna-1),
                    Pair(linha, coluna+1), Pair(linha + 1, coluna - 1), Pair(linha + 1, coluna), Pair(linha + 1, coluna + 1)))}
            "S"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha + 4, coluna-1), Pair(linha + 4, coluna),
                    Pair(linha + 4, coluna + 1), Pair(linha + 3, coluna-1), Pair(linha + 3, coluna + 1), Pair(linha + 2, coluna-1),
                    Pair(linha + 2, coluna + 1), Pair(linha + 1, coluna-1), Pair(linha + 1, coluna + 1), Pair(linha, coluna-1),
                    Pair(linha, coluna+1), Pair(linha - 1, coluna - 1), Pair(linha - 1, coluna), Pair(linha - 1, coluna + 1)))}
            "E"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna-1), Pair(linha - 1, coluna),
                    Pair(linha - 1, coluna + 1), Pair(linha - 1, coluna + 2), Pair(linha - 1, coluna + 3), Pair(linha - 1, coluna + 4),
                    Pair(linha, coluna-1), Pair(linha, coluna + 4), Pair(linha + 1, coluna-1), Pair(linha + 1, coluna),
                    Pair(linha + 1, coluna + 1), Pair(linha + 1, coluna + 2), Pair(linha + 1, coluna + 3), Pair(linha + 1, coluna + 4)))}
            "O"->{coordenadas = juntarCoordenadas(coordenadas, arrayOf(Pair(linha - 1, coluna+1), Pair(linha - 1, coluna),
                    Pair(linha - 1, coluna - 1), Pair(linha - 1, coluna - 2), Pair(linha - 1, coluna - 3), Pair(linha - 1, coluna - 4),
                    Pair(linha, coluna+1), Pair(linha, coluna - 4), Pair(linha + 1, coluna+1), Pair(linha + 1, coluna),
                    Pair(linha + 1, coluna - 1), Pair(linha + 1, coluna - 2), Pair(linha + 1, coluna - 3), Pair(linha + 1, coluna - 4)))}
        }
    }
    coordenadas = limparCoordenadasVazias(coordenadas)
    var resultado = emptyArray<Pair<Int, Int>>()
    for (coordenada in coordenadas) {
        if (coordenadaContida(tabuleiro, coordenada.first, coordenada.second)) {
            resultado += coordenada
        }
    }
    return resultado
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int,Int>>): Boolean{
    var livre = true

    for ((linha,coluna) in coordenadas) {
        if (!coordenadaContida(tabuleiro,linha,coluna)) {
            livre = false
        }
    }

    for ((linha,coluna) in coordenadas) {
        if (tabuleiro[linha - 1][coluna - 1] != null) {
            livre = false
        }
    }

    return livre
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val coordenadasJuntas = juntarCoordenadas(
            gerarCoordenadasNavio(tabuleiro, linha, coluna, "E", dimensao),
            gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)
    )
    val navioChar = when(dimensao) {
        1 -> '1'
        2 -> '2'
        3 -> '3'
        4 -> '4'
        else -> return false
    }
    if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || coluna + dimensao - 1 !in 1..tabuleiro[0].size) {
        return false
    }
    for (i in 0 until dimensao) {
        if (tabuleiro[linha - 1][(coluna - 1) + i] != null) {
            return false
        }
    }
    if (!estaLivre(tabuleiro, coordenadasJuntas)) {
        return false
    }
    for (i in 0 until dimensao) {
        tabuleiro[linha - 1][(coluna - 1) + i] = navioChar
    }
    return true
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean{
    val coordenadasJuntas = juntarCoordenadas(
            gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao),
            gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)
    )
    if (!estaLivre(tabuleiro, coordenadasJuntas)) {
        return false
    }
    val navioChar = when(dimensao) {
        1 -> '1'
        2 -> '2'
        3 -> '3'
        4 -> '4'
        else -> return false
    }
    if (orientacao == "N"){
        if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || linha - dimensao + 1 !in 1..tabuleiro.size) {
            return false
        }
        for (i in 0 until dimensao) {
            if (tabuleiro[(linha - 1) - i][coluna - 1] != null) {
                return false
            }
        }
        for (i in 0 until dimensao) {
            tabuleiro[(linha - 1) - i][coluna - 1] = navioChar
        }
        return true
    }else if (orientacao == "S"){
        if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || linha + dimensao - 1 !in 1..tabuleiro.size) {
            return false
        }
        for (i in 0 until dimensao) {
            if (tabuleiro[(linha - 1) + i][coluna - 1] != null) {
                return false
            }
        }
        for (i in 0 until dimensao) {
            tabuleiro[(linha - 1) + i][coluna - 1] = navioChar
        }
        return true
    }else if (orientacao == "E"){
        return insereNavioSimples(tabuleiro,linha,coluna,dimensao)
    }else if (orientacao == "O"){
        if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || coluna - dimensao + 1 !in 1..tabuleiro[0].size) {
            return false
        }
        for (i in 0 until dimensao) {
            if (tabuleiro[linha - 1][(coluna - 1) - i] != null) {
                return false
            }
        }
        for (i in 0 until dimensao) {
            tabuleiro[linha - 1][(coluna - 1) - i] = navioChar
        }
        return true
    }else return false
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, configuracao: Array<Int>): Array<Array<Char?>>{
    var linha: Int
    var coluna: Int
    var orientacao: String
    var dimensao = 0
    for (i in configuracao.size-1 downTo  0) {
        if (i == 0){
            dimensao = 1
        }else if (i == 1){
            dimensao = 2
        }else if (i == 2){
            dimensao = 3
        }else if (i == 3){
            dimensao = 4
        }
        if (configuracao[i]>0){
            for (j in 1..configuracao[i]){
                do {
                    linha = (1..tabuleiro.size).random()
                    coluna = (1..tabuleiro[0].size).random()
                    orientacao = arrayOf("N", "S", "E", "O").random()
                } while (!insereNavio(tabuleiro,linha,coluna,orientacao,dimensao))
            }
        }
    }
    return tabuleiro
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean{
    var resultado = false
    val coordenada = Pair(linha-1,coluna-1)
    if (coordenadaContida(tabuleiro, linha, coluna)){
        if (tabuleiro[coordenada.first][coordenada.second] == '1') {
            resultado = true
        }else if (tabuleiro[coordenada.first][coordenada.second] == '2'){
            if (coordenada.second+1 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second+1] == '2'){
                resultado = true
            }else if(coordenada.second-1 >= 0 && tabuleiro[coordenada.first][coordenada.second-1] == '2'){
                resultado = true
            }else if(coordenada.first+1 < tabuleiro.size && tabuleiro[coordenada.first+1][coordenada.second] == '2'){
                resultado = true
            }else if(coordenada.first-1 >= 0 && tabuleiro[coordenada.first-1][coordenada.second] == '2'){
                resultado = true
            }else resultado = false
        }else if (tabuleiro[coordenada.first][coordenada.second] == '3'){
            if (coordenada.second+2 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second+1] == '3' &&
                    tabuleiro[coordenada.first][coordenada.second+2] == '3'){
                resultado = true
            }else if(coordenada.second-2 >= 0 && tabuleiro[coordenada.first][coordenada.second-1] == '3' &&
                    tabuleiro[coordenada.first][coordenada.second-2] == '3'){
                resultado = true
            }else if(coordenada.first+2 < tabuleiro.size && tabuleiro[coordenada.first+1][coordenada.second] == '3' &&
                    tabuleiro[coordenada.first+2][coordenada.second] == '3'){
                resultado = true
            }else if(coordenada.first-2 >= 0 && tabuleiro[coordenada.first-1][coordenada.second] == '3' &&
                    tabuleiro[coordenada.first-2][coordenada.second] == '3'){
                resultado = true
            }else if(coordenada.first-1 >= 0 && coordenada.first+1 < tabuleiro.size &&
                    tabuleiro[coordenada.first-1][coordenada.second] == '3' && tabuleiro[coordenada.first+1][coordenada.second] == '3'){
                resultado = true
            }else if(coordenada.second-1 >= 0 && coordenada.second+1 < tabuleiro[0].size && coordenada.first+1 < tabuleiro.size &&
                    tabuleiro[coordenada.first][coordenada.second-1] == '3' && tabuleiro[coordenada.first][coordenada.second+1] == '3'){
                resultado = true
            } else resultado = false
        }else if (tabuleiro[coordenada.first][coordenada.second] == '4'){
            if (coordenada.second+3 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second+1] == '4' &&
                    tabuleiro[coordenada.first][coordenada.second+2] == '4' && tabuleiro[coordenada.first][coordenada.second+3] == '4'){
                resultado = true
            }else if(coordenada.second-3 >= 0 && tabuleiro[coordenada.first][coordenada.second-1] == '4' &&
                    tabuleiro[coordenada.first][coordenada.second-2] == '4' && tabuleiro[coordenada.first][coordenada.second-3] == '4'){
                resultado = true
            }else if(coordenada.first+3 < tabuleiro.size && tabuleiro[coordenada.first+1][coordenada.second] == '4' &&
                    tabuleiro[coordenada.first+2][coordenada.second] == '4' && tabuleiro[coordenada.first+3][coordenada.second] == '4'){
                resultado = true
            }else if(coordenada.first-3 >= 0 && tabuleiro[coordenada.first-1][coordenada.second] == '4' &&
                    tabuleiro[coordenada.first-2][coordenada.second] == '4' && tabuleiro[coordenada.first-3][coordenada.second] == '4'){
                resultado = true
            }else if(coordenada.first-1 >= 0 && coordenada.first+2 < tabuleiro.size && tabuleiro[coordenada.first-1][coordenada.second] == '4' &&
                    tabuleiro[coordenada.first+1][coordenada.second] == '4' && tabuleiro[coordenada.first+2][coordenada.second] == '4'){
                resultado = true
            }else if(coordenada.first-2 >= 0 && coordenada.first+1 < tabuleiro.size && tabuleiro[coordenada.first-2][coordenada.second] == '4' &&
                    tabuleiro[coordenada.first-1][coordenada.second] == '4' && tabuleiro[coordenada.first+1][coordenada.second] == '4'){
                resultado = true
            }else if(coordenada.second-1 >= 0 && coordenada.second+2 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second-1] == '4' &&
                    tabuleiro[coordenada.first][coordenada.second+1] == '4' && tabuleiro[coordenada.first][coordenada.second+2] == '4'){
                resultado = true
            }else if(coordenada.second-2 >= 0 && coordenada.second+1 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second-2] == '4' &&
                    tabuleiro[coordenada.first][coordenada.second-1] == '4' && tabuleiro[coordenada.first][coordenada.second+1] == '4'){
                resultado = true
            } else resultado = false
        }else resultado = false
    }
    return resultado
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, valor: Boolean): Array<String>{
    var resultado1: String
    val resultado2 = Array<String>(tabuleiro.size+1){""}
    if (valor == true){
        resultado2[0] = "| ${criaLegendaHorizontal(tabuleiro[0].size)} |"
        for (linha in 1 ..tabuleiro.size) {
            resultado1 = ""
            for (coluna in 1 ..tabuleiro[0].size) {
                if (tabuleiro[linha-1][coluna-1] == null) {
                    resultado1 += "| ~ "
                }else if (tabuleiro[linha-1][coluna-1] == '1') {
                    resultado1 += "| 1 "
                }else if (tabuleiro[linha-1][coluna-1] == '2') {
                    resultado1 += "| 2 "
                }else if (tabuleiro[linha-1][coluna-1] == '3') {
                    resultado1 += "| 3 "
                }else if (tabuleiro[linha-1][coluna-1] == '4') {
                    resultado1 += "| 4 "
                }
            }
            resultado2[linha] = "$resultado1| $linha"
        }
    }else if (valor == false){
        resultado2[0] = "| ${criaLegendaHorizontal(tabuleiro[0].size)} |"
        for (linha in 1 ..tabuleiro.size) {
            resultado1 = ""
            for (coluna in 1 ..tabuleiro[0].size) {
                if (tabuleiro[linha-1][coluna-1] == null) {
                    resultado1 += "| ? "
                }else if (tabuleiro[linha-1][coluna-1] == 'X') {
                    resultado1 += "| X "
                }else if (tabuleiro[linha-1][coluna-1] == '1') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        resultado1 += "| 1 "
                    }else resultado1 += "| \u2081 "
                }else if (tabuleiro[linha-1][coluna-1] == '2') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        resultado1 += "| 2 "
                    }else resultado1 += "| \u2082 "
                }else if (tabuleiro[linha-1][coluna-1] == '3') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        resultado1 += "| 3 "
                    }else resultado1 += "| \u2083 "
                }else if (tabuleiro[linha-1][coluna-1] == '4') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        resultado1 += "| 4 "
                    }else resultado1 += "| \u2084 "
                }
            }
            resultado2[linha] = "$resultado1| $linha"
        }
    }
    return resultado2
}

fun lancarTiro(tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenada: Pair<Int,Int>): String{
    var resultado = ""
    if (tabuleiroReal[coordenada.first-1][coordenada.second-1] == null) {
        tabuleiroPalpites[coordenada.first-1][coordenada.second-1] = 'X'
        resultado = "Agua."
    }else if (tabuleiroReal[coordenada.first-1][coordenada.second-1] == '1'){
        tabuleiroPalpites[coordenada.first-1][coordenada.second-1] = '1'
        resultado = "Tiro num submarino."
    }else if (tabuleiroReal[coordenada.first-1][coordenada.second-1] == '2'){
        tabuleiroPalpites[coordenada.first-1][coordenada.second-1] = '2'
        resultado = "Tiro num contra-torpedeiro."
    }else if (tabuleiroReal[coordenada.first-1][coordenada.second-1] == '3'){
        tabuleiroPalpites[coordenada.first-1][coordenada.second-1] = '3'
        resultado = "Tiro num navio-tanque."
    }else if (tabuleiroReal[coordenada.first-1][coordenada.second-1] == '4'){
        tabuleiroPalpites[coordenada.first-1][coordenada.second-1] = '4'
        resultado = "Tiro num porta-avioes."
    }
    return resultado
}

fun geraTiroComputador(tabuleiro: Array<Array<Char?>>): Pair<Int,Int>{
    var coordenada = Pair((1..tabuleiro.size).random(),(1..tabuleiro[0].size).random())
    while(!coordenadaContida(tabuleiro,coordenada.first,coordenada.second) || tabuleiro[coordenada.first-1][coordenada.second-1] != null){
        coordenada = Pair((1..tabuleiro.size).random(),(1..tabuleiro[0].size).random())
    }
    return coordenada
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int{
    var count = 0
    if (dimensao == 1){
        for (linha in 1 ..tabuleiro.size) {
            for (coluna in 1 ..tabuleiro[0].size) {
                if (tabuleiro[linha-1][coluna-1] == '1') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        count++
                    }
                }
            }
        }
    } else if (dimensao == 2){
        for (linha in 1 ..tabuleiro.size) {
            for (coluna in 1 ..tabuleiro[0].size) {
                if (tabuleiro[linha-1][coluna-1] == '2') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        count++
                    }
                }
            }
        }
        count /= 2
    }else if (dimensao == 3){
        for (linha in 1 ..tabuleiro.size) {
            for (coluna in 1 ..tabuleiro[0].size) {
                if (tabuleiro[linha-1][coluna-1] == '3') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        count++
                    }
                }
            }
        }
        count /= 3
    }else if (dimensao == 4){
        for (linha in 1 ..tabuleiro.size) {
            for (coluna in 1 ..tabuleiro[0].size) {
                if (tabuleiro[linha-1][coluna-1] == '4') {
                    if (navioCompleto(tabuleiro,linha,coluna)){
                        count++
                    }
                }
            }
        }
        count /= 4
    }
    return count
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean{
    val numNavios = calculaNumNavios(tabuleiro.size, tabuleiro[0].size)
    val dimensao1 = contarNaviosDeDimensao(tabuleiro,1)
    val dimensao2 = contarNaviosDeDimensao(tabuleiro, 2)
    val dimensao3 = contarNaviosDeDimensao(tabuleiro, 3)
    val dimensao4 = contarNaviosDeDimensao(tabuleiro, 4)
    if (numNavios[0] == dimensao1){
        if (numNavios[1] == dimensao2){
            if (numNavios[2] == dimensao3){
                if (numNavios[3] == dimensao4){
                    return true
                }else return false
            }else return false
        }else return false
    }else return false
}


fun lerJogo (nomeFicheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {
    val ficheiroGravado = File(nomeFicheiro).readLines()
    numLinhas = ficheiroGravado[0][0].digitToInt()
    numColunas = ficheiroGravado[0][2].digitToInt()
    val linhasNoFicheiro = 3 * tipoTabuleiro + 1 + numLinhas * (tipoTabuleiro- 1) until 3 * tipoTabuleiro + 1 + numLinhas * tipoTabuleiro
    val linhasNoFicheiroMin = 3 * tipoTabuleiro + 1 + numLinhas * (tipoTabuleiro- 1)
    val tabuleiro = criaTabuleiroVazio(numLinhas,numColunas)
    var linha = linhasNoFicheiroMin
    while(linha in linhasNoFicheiro){
        var coluna = 0
        var contaVirgulas = 0
        while (contaVirgulas < numColunas && coluna < ficheiroGravado [linha].length) {
            when (ficheiroGravado[linha] [coluna]){
                ',' -> contaVirgulas ++
                '1', '2', '3','4', 'X' -> tabuleiro[linha - linhasNoFicheiroMin][contaVirgulas] = ficheiroGravado[linha][coluna]
            }
            coluna++
        }
        linha++
    }
    return tabuleiro
}


fun gravarTabuleiro(jogoGravado: PrintWriter, tabuleiro: Array<Array<Char?>>){
    for(linha in  0 until tabuleiro.size) {
        jogoGravado.println()
        for (coluna in 0 until tabuleiro[0].size) {
            when (tabuleiro[linha][coluna]) {
                'X' -> jogoGravado.print("X")
                '1', '2', '3', '4' -> jogoGravado.print(tabuleiro[linha][coluna])
            }
            if (coluna < tabuleiro[0].size - 1) {
                jogoGravado.print(",")
            }

        }
    }
}

fun gravarJogo(nomeDoFicheiro: String, tabuleiroRealHumano: Array<Array<Char?>>, tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesDoComputador: Array<Array<Char?>>){
    val jogoGravado = File(nomeDoFicheiro).printWriter()
    jogoGravado.print("${tabuleiroRealHumano.size},${tabuleiroRealHumano[0].size}")
    jogoGravado.print("\n\nJogador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroRealHumano)
    jogoGravado.print("\n\nJogador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesHumano)
    jogoGravado.print("\n\nComputador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroRealComputador)
    jogoGravado.print("\n\nComputador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesDoComputador)
    jogoGravado.close()
    return
}

fun colocarNaviosHumano(){
    val numNavios = calculaNumNavios(tabuleiroHumano.size,tabuleiroHumano[0].size)
    var dimensao = 0
    var nomeNavio = ""
    for (i in 0 until numNavios.size) {
        if (i == 0){
            dimensao = 1
            nomeNavio = "submarino"
        }else if (i == 1){
            dimensao = 2
            nomeNavio = "contra-torpedeiro"
        }else if (i == 2){
            dimensao = 3
            nomeNavio = "navio-tanque"
        }else if (i == 3){
            dimensao = 4
            nomeNavio = "porta-avioes"
        }
        if (numNavios[i]>0){
            for (j in 1..numNavios[i]){
                println("Insira as coordenadas de um $nomeNavio:\nCoordenadas? (ex: 6,G)")
                var coordenadas = readln()
                if (coordenadas == "-1") {
                    return main()
                }
                var coordenadaProcessada = processaCoordenadas(coordenadas, numLinhas, numColunas)
                if (dimensao == 1){
                    while (coordenadaProcessada == null || coordenadas == "" ||
                            !insereNavio(tabuleiroHumano,coordenadaProcessada.first,coordenadaProcessada.second,"E",dimensao)) {
                        println("!!! Posicionamento invalido, tente novamente\nInsira as coordenadas de um $nomeNavio:\nCoordenadas? (ex: 6,G)")
                        coordenadas = readln()
                        if (coordenadas == "-1") {
                            return main()
                        }
                        coordenadaProcessada = processaCoordenadas(coordenadas, numLinhas, numColunas)
                    }
                }else{
                    println("Insira a orientacao do navio:\nOrientacao? (N, S, E, O)")
                    var orientacao = readln()
                    if (orientacao == "-1"){
                        return main()
                    }
                    while (coordenadaProcessada == null || coordenadas == "" ||
                            !insereNavio(tabuleiroHumano,coordenadaProcessada.first,coordenadaProcessada.second,orientacao,dimensao)) {
                        println("!!! Posicionamento invalido, tente novamente\nInsira as coordenadas de um $nomeNavio:\nCoordenadas? (ex: 6,G)")
                        coordenadas = readln()
                        if (coordenadas == "-1") {
                            return main()
                        }
                        coordenadaProcessada = processaCoordenadas(coordenadas, numLinhas, numColunas)
                        println("Insira a orientacao do navio:\nOrientacao? (N, S, E, O)")
                        orientacao = readln()
                        if (orientacao == "-1"){
                            return main()
                        }
                    }
                }
                val tabueleiroFeito = obtemMapa(tabuleiroHumano,true)
                for (linha in 0 until tabueleiroFeito.size){
                    println(tabueleiroFeito[linha])
                }
            }
        }
    }
}

fun opcaoMenu1() {
    println("\n> > Batalha Naval < <\n\nDefina o tamanho do tabuleiro:\nQuantas linhas?")
    var linhasTabuleiro = readln().toIntOrNull()
    while (linhasTabuleiro == null) {
        println("!!! Número de linhas invalidas, tente novamente\nQuantas linhas?")
        linhasTabuleiro = readln().toIntOrNull()
    }
    if (linhasTabuleiro == -1) {
        return
    }
    println("Quantas colunas?")
    var colunasTabuleiro = readln().toIntOrNull()
    while (colunasTabuleiro == null) {
        println("!!! Número de linhas colunas, tente novamente\nQuantas colunas?")
        colunasTabuleiro = readln().toIntOrNull()
    }
    if (colunasTabuleiro == -1) {
        return
    }
    numLinhas = linhasTabuleiro
    numColunas = colunasTabuleiro
    if (tamanhoTabuleiroValido(numLinhas, numColunas)) {
        tabuleiroHumano = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas,numColunas)
        var tabueleiroFeito = obtemMapa(tabuleiroHumano,true)
        for (linha in 0 until tabueleiroFeito.size){
            println(tabueleiroFeito[linha])
        }
        colocarNaviosHumano()
        val configuracao = calculaNumNavios(numLinhas, numColunas)
        tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroPalpitesDoComputador =criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroComputador = preencheTabuleiroComputador(tabuleiroComputador,configuracao)
        println("Pretende ver o mapa gerado para o Computador? (S/N)")
        val escolha = readln()
        if (escolha=="S"){
            tabueleiroFeito = obtemMapa(tabuleiroComputador,true)
            for (linha in 0 until tabueleiroFeito.size){
                println(tabueleiroFeito[linha])
            }
            return
        }else if (escolha=="N"){
            return
        }
    }
return
}
fun opcaoMenu2(){
    var enter: String
    if (numLinhas == -1 && numColunas == -1) {
        println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
        return
    }else{
        while (!venceu(tabuleiroPalpitesDoComputador) && !venceu(tabuleiroPalpitesDoHumano)){
            val tabueleiroFeito = obtemMapa(tabuleiroPalpitesDoHumano,false)
            for (linha in 0 until tabueleiroFeito.size){
                println(tabueleiroFeito[linha])
            }
            println("Indique a posição que pretende atingir\nCoordenadas? (ex: 6,G)")
            var coordenadas = readln()
            if (coordenadas == "-1") {
                return
            }
            var coordenadasProcessadas = processaCoordenadas(coordenadas,numLinhas,numColunas)
            while (coordenadasProcessadas == null ) {
                println("!!! Posicionamento invalido, tente novamente\nIndique a posição que pretende atingir\nCoordenadas? (ex: 6,G)")
                coordenadas = readln()
                if (coordenadas == "-1") {
                    return
                }
                coordenadasProcessadas = processaCoordenadas(coordenadas, numLinhas, numColunas)
            }
            var alvo = lancarTiro(tabuleiroComputador,tabuleiroPalpitesDoHumano, coordenadasProcessadas)
            if (!navioCompleto(tabuleiroPalpitesDoHumano,coordenadasProcessadas.first,coordenadasProcessadas.second)){
                println(">>> HUMANO >>>$alvo")
            }else println(">>> HUMANO >>>$alvo Navio ao fundo!")
            if (!venceu(tabuleiroPalpitesDoComputador) && !venceu(tabuleiroPalpitesDoHumano)){
                val tiroComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
                println("Computador lancou tiro para a posicao $tiroComputador")
                alvo = lancarTiro(tabuleiroHumano,tabuleiroPalpitesDoComputador,tiroComputador)
                if (!navioCompleto(tabuleiroPalpitesDoComputador,tiroComputador.first,tiroComputador.second)){
                    println(">>> COMPUTADOR >>>$alvo")
                }else println(">>> COMPUTADOR >>>$alvo Navio ao fundo!")
                println("Prima enter para continuar")
                enter = readln()
            }
        }
        if (venceu(tabuleiroPalpitesDoHumano)){
            println("PARABENS! Venceu o jogo!\nPrima enter para voltar ao menu principal")
            enter = readln()
        }else{
            println("OPS! O computador venceu o jogo!\nPrima enter para voltar ao menu principal")
            enter = readln()
        }
    }
}

fun opcaoMenu3(){
    if (numLinhas == -1 && numColunas == -1){
        println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
        return
    }else{
        println("Introduza o nome do ficheiro (ex: jogo.txt)")
        val nomeDoFicheiro = readln()
        if (nomeDoFicheiro == "-1") {
            return
        }
        gravarJogo(nomeDoFicheiro,tabuleiroHumano,tabuleiroPalpitesDoHumano,tabuleiroComputador,tabuleiroPalpitesDoComputador)
        println("Tabuleiro ${numLinhas}x$numColunas gravado com sucesso")
    }
}

fun opcaoMenu4(){
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readln()
    if (nomeDoFicheiro == "-1") {
        return
    }
    tabuleiroHumano = lerJogo(nomeDoFicheiro,1)
    tabuleiroPalpitesDoHumano = lerJogo(nomeDoFicheiro,2)
    tabuleiroComputador = lerJogo(nomeDoFicheiro,3)
    tabuleiroPalpitesDoComputador = lerJogo(nomeDoFicheiro,4)
    println("Tabuleiro ${numLinhas}x$numColunas lido com sucesso")
    val tabueleiroFeito = obtemMapa(tabuleiroHumano,true)
    for (linha in 0 until tabueleiroFeito.size){
        println(tabueleiroFeito[linha])
    }
}

fun main() {
    while (true) {
        println("\n> > Batalha Naval < <\n\n1 - Definir Tabuleiro e Navios\n2 - Jogar\n3 - Gravar\n4 - Ler\n0 - Sair\n")
        var opcaoMenu = readln().toIntOrNull()
        while(opcaoMenu == null ||(opcaoMenu < 0 || opcaoMenu > 4)) {
            println("!!! Opcao invalida, tente novamente")
            opcaoMenu = readln().toIntOrNull()
        }
        when (opcaoMenu) {
            0 -> return
            1 -> opcaoMenu1()
            2->opcaoMenu2()
            3->opcaoMenu3()
            4->opcaoMenu4()
            else -> return
        }
    }
}





