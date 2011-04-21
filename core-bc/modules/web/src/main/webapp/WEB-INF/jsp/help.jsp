<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

 
<div class="help-block help-block-color">
    <h1>Välkommen till vertikala prioriteringar!</h1>
    <h2>Så här använder du vertikala prioriteringar</h2>
    <ol>
      <li>
          Välj ett eller flera sektorsråd i vänstra listan för att visa listan med prioriteringsobjekt
      </li>
      <li>
          Utnyttja filtrering för att hitta ett specifikt prioriteringsobjekt eller för att analysera en grupp av prioriteringsobjekt
          <ol>
            <li class="no-list-style"><img src="img/tratt_unselected.png" /> Tryck på ikonen för att aktiverings kolumnens filtrering</li>
            <li class="no-list-style"><img src="img/tratt_selected.png" /> Visar att en kolumn har en aktiv filtrering</li>
            <li class="no-list-style"><img src="img/x.png" /> Ta bort filtrering på kolumn</li>
            <li class="no-list-style"><img src="img/arrow_left.png" /> Dölj sektorsråd</li>
            <li class="no-list-style"><img src="img/arrow_right.png" /> Visa sektorsråd</li>
            <li class="no-list-style"><img src="img/arrow-down.png" /> Sortera kolumn</li>
            <li class="no-list-style"><img src="img/information.png" /> Visa information</li>
          </ol>
      </li>
      <li>
          Välj vilka kolumner som skall synas med <img src="img/show_hide_columns.png" />
      </li>
      <li>
          För att se detaljvy av ett prioriteringsobjekt markera raden och tryck på <br/> <img src="img/show_prioriteringsobjekt.png" /> denna knapp syns då det finns minst ett prioriteringsobjekt listan
      </li>
      <li>
          För att förändra ett prioriteringsobjekt krävs att du är inloggad
      </li>
      <c:if test="${loginResult && user != null and user.editor}">
        <li>
          Allt efter situation dyker symboler upp i detaljvyn som informerar om informationens status
          <ol>
            <li class="no-list-style"><img src="img/flag_white.gif" /> Markerar värde som skiljer sig ifrån godkänd version</li>
            <li class="no-list-style"><img src="img/changed.png" /> Markerar värde som ännu inte sparats</li>
          </ol>
        </li>
      </c:if>
    </ol>
</div>
