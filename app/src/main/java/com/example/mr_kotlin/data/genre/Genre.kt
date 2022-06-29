package com.example.mr_kotlin.data.genre

class Genre (
    private var action: Int,
    private var adventure: Int,
    private var animation: Int,
    private var children: Int,
    private var comedy: Int,
    private var crime: Int,
    private var documentary: Int,
    private var drama: Int,
    private var fantasy: Int,
    private var filmNoir: Int,
    private var horror: Int,
    private var musicalMystery: Int,
    private var romance: Int,
    private var sciFi: Int,
    private var thriller: Int,
    private var warWestern: Int,
    private var mystery: Int,
    private var war: Int,
    private var musical: Int,
    private var IMAX: Int,
    private var western: Int,
    private var noGenresListed: Int,

    private var genresPieChart: MutableMap<String, Double> = mutableMapOf(
        "Action" to 0.0,
        "Adventure" to 0.0,
        "Animation" to 0.0,
        "Children" to 0.0,
        "Comedy" to 0.0,
        "Crime" to 0.0,
        "Documentary" to 0.0,
        "Drama" to 0.0,
        "Fantasy" to 0.0,
        "FlimNoir" to 0.0,
        "Horror" to 0.0,
        "MusicalMystery" to 0.0,
        "Romance" to 0.0,
        "SciFi" to 0.0,
        "Thriller" to 0.0,
        "WarWestern" to 0.0,
        "Mystery" to 0.0,
        "War" to 0.0,
        "Musical" to 0.0,
        "IMAX" to 0.0,
        "Western" to 0.0,
        "noGenresListed" to 0.0
    ),

    ) {

    fun getGenresPieChart(): Map<String, Double> {
        return genresPieChart
    }

    fun setGenrePieChart(name: String , percentaje: Double){
        this.genresPieChart[name]= percentaje
    }



    fun getAction(): Int {
        return action
    }
    fun setAction(param: Int){
        this.action = param
    }

    fun getAdventure(): Int {
        return adventure
    }
    fun getAdventure(param: Int){
        this.adventure = param
    }


    fun getAnimation(): Int {
        return animation
    }
    fun setAnimation(param: Int){
        this.animation = param
    }


    fun getChildren(): Int {
        return children
    }
    fun setChildren(param: Int){
        this.children = param
    }


    fun getComedy(): Int {
        return comedy
    }
    fun setComedy(param: Int){
        this.comedy = param
    }


    fun getCrime(): Int {
        return crime
    }
    fun setCrime(param: Int){
        this.crime = param
    }


    fun getDocumentary(): Int {
        return documentary
    }
    fun setDocumentary(param: Int){
        this.documentary = param
    }


    fun getDrama(): Int {
        return drama
    }
    fun setDrama(param: Int){
        this.drama = param
    }


    fun getFantasy(): Int {
        return fantasy
    }
    fun setFantasy(param: Int){
        this.fantasy = param
    }


    fun getFilmNoir(): Int {
        return filmNoir
    }
    fun setFilmNoir(param: Int){
        this.filmNoir = param
    }


    fun getHorror(): Int {
        return horror
    }
    fun setHorror(param: Int){
        this.horror = param
    }


    fun getMusicalMystery(): Int {
        return musicalMystery
    }
    fun setMusicalMystery(param: Int){
        this.musicalMystery = param
    }


    fun getRomance(): Int {
        return romance
    }
    fun setRomance(param: Int){
        this.romance = param
    }


    fun getSciFi(): Int {
        return sciFi
    }
    fun setSciFi(param: Int){
        this.sciFi = param
    }


    fun getThriller(): Int {
        return thriller
    }
    fun setThriller(param: Int){
        this.thriller = param
    }


    fun getWarWestern(): Int {
        return warWestern
    }
    fun setWarWestern(param: Int){
        this.warWestern = param
    }


    fun getMystery(): Int {
        return mystery
    }
    fun setMystery(param: Int){
        this.mystery = param
    }


    fun getWar(): Int {
        return war
    }
    fun setWar(param: Int){
        this.war = param
    }


    fun getIMAX(): Int {
        return IMAX
    }
    fun setIMAX(param: Int){
        this.IMAX = param
    }


    fun getMusical(): Int {
        return musical
    }
    fun setMusical(param: Int){
        this.musical = param
    }


    fun getWestern(): Int {
        return western
    }
    fun setWestern(param: Int){
        this.western = param
    }


    fun getNoGenresListed(): Int {
        return noGenresListed
    }
    fun setNoGenresListed(param: Int){
        this.noGenresListed = param
    }

    companion object {

    }

}