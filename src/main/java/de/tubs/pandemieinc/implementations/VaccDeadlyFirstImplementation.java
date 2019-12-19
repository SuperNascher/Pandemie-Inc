package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.Round;
import de.tubs.pandemieinc.ActionHelper;
import de.tubs.pandemieinc.Action;
import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.*;
import de.tubs.pandemieinc.Strength;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.function.Supplier;

public class VaccDeadlyFirstImplementation {

    public Round round;
    final int quarantineRoundlength = 2; //ändern wenn fastestFirst

    public VaccDeadlyFirstImplementation(Round round) {
        this.round = round;

    }

    public String selectAction() {
	    if(this.round.points < 40) {
	        return ActionPrinter.endRound();
	    }
	    List<Object[]> singleCityPathogens = this.getSingleCityPathogens();
	    City city = null;
	    for(int i = 0; i < singleCityPathogens.size(); i++) {
	        Object[] array = singleCityPathogens.get(i);
	        city = (City) array[1];
	        List<BaseEvent> events = city.events;

            for(BaseEvent temp : events) {
                if(temp.getClass().getSimpleName().equals("QuarantineEvent") ){ 
                    singleCityPathogens.remove(i);
                    i--;             
                }
                
            }
        }        	    
	    if(singleCityPathogens.size() > 0) {
            Pathogen deadlyPath = getDeadlyPathogen(singleCityPathogens);
            city = null;
            City pathCity = null;
            for(Object[] temp : singleCityPathogens) {
                
	            city = (City) temp[1];
	            Pathogen pathogen = (Pathogen) temp[0];
                if(pathogen == deadlyPath) {
                    pathCity = city;
                }
            }
            this.round.points -= (Action.PUTUNDERQUARANTINE.baseCost + quarantineRoundlength*Action.PUTUNDERQUARANTINE.roundCost);
            if(pathCity != null) {
	            return  ActionPrinter.putUnderQuarantine(pathCity, quarantineRoundlength);
            }    
            
	    }
	    if(this.round.points < Action.DEVELOPVACCINE.baseCost-20) {
	        return ActionPrinter.endRound();
	    }
	    List<Pathogen> acutePathogens = acutePathCounter();
	    if(acutePathogens.size() > 1) {
	        Pathogen vaccPath = getSlowPathogen(acutePathogens);
	        if(!checkVaccDev(vaccPath) && !checkVacc(vaccPath) && this.round.points - Action.DEVELOPVACCINE.baseCost >= 0) {
	            this.round.points -= Action.DEVELOPVACCINE.baseCost;
	            
	            return ActionPrinter.developVaccine(vaccPath);
	            	        
	        } else if(checkVacc(vaccPath) && this.round.points - Action.DEPLOYVACCINE.baseCost >= 20) {
	            List<City> cities = this.round.cities.values().stream()
                .collect(Collectors.toList());
                                
                city = null;
                int cultureValue = -9;
                int tempCulture;
                
                for(City tempC : getNewCities(findNewestCity(cities, vaccPath), cities)) {
	                if(cityInfectedCheck(tempC, vaccPath) || vaccineDeployedCheck(tempC, vaccPath)) {
	                    continue;
	                }
                    tempCulture = tempC.economy.level + tempC.government.level + tempC.hygiene.level + tempC.awareness.level;
	                if(cultureValue < tempCulture) {
	                    city = tempC;
	                    cultureValue = tempCulture;
	                }
	            }
	            if(city != null) {
	                this.round.points -= Action.DEPLOYVACCINE.baseCost;
                    return ActionPrinter.deployVaccine(vaccPath, city);
	            }
	        }
	        return ActionPrinter.endRound();
	    
	    } else if(acutePathogens.size() == 1) {
	        Pathogen medPath = getSlowPathogen(acutePathogens);
	        if(!checkMedDev(medPath) && !checkMed(medPath) && this.round.points - Action.DEVELOPMEDICATION.baseCost >= 20) {
	             this.round.points -= Action.DEVELOPMEDICATION.baseCost;
	            return ActionPrinter.developMedication(medPath);
	        } else if(checkMed(medPath) && this.round.points - Action.DEPLOYMEDICATION.baseCost >= 20) {
	            
	            List<City> cities = this.round.cities.values().stream()
                .collect(Collectors.toList());
                city = null;
                double infectedPopTemp = 0;
                double infectedPop = 0;
                double prevalence = 0;
 
                for(City tempC : cities) {
	                if(!cityInfectedCheck(tempC, medPath)) {
	                    continue;	                
	                }
	                for(BaseEvent tempE : tempC.events) {
                        if (tempE.getClass().getSimpleName().equals("OutbreakEvent")){
                            OutbreakEvent event = (OutbreakEvent) tempE;
                            if (event.pathogen == medPath){
                                prevalence = event.prevalence;                
                            }
                        }
                    }                        

                    infectedPopTemp = tempC.population*prevalence;
	                if(infectedPop < infectedPopTemp) {
	                    city = tempC;
	                    infectedPop = infectedPopTemp;
	                }
	            }
	            
	            if(city != null) {
	                this.round.points -= Action.DEPLOYMEDICATION.baseCost;
                    return ActionPrinter.deployMedication(medPath, city);
	            }
	        }
	    
	    
	    }
        
        
        
        return ActionPrinter.endRound();
    }
        
    
    public List<Object[]> getSingleCityPathogens() {
    
        List<Object[]> singleCityPathogen = new ArrayList<>();
        List<Pathogen> pathogens = this.round.pathogens.values().stream()
            .collect(Collectors.toList());
        List<City> cities = this.round.cities.values().stream()
            .collect(Collectors.toList());
        
        for(Pathogen tempP : pathogens) {
            int pathCount = 0;
            City pathCity = null;
            for(City tempC : cities) {
                List<BaseEvent> events = tempC.events;
                for(BaseEvent tempE : events) {                    
                    if (tempE.getClass().getSimpleName().equals("OutbreakEvent")) {
                        OutbreakEvent outEvent = (OutbreakEvent) tempE;
                        if (tempP.name.equals(outEvent.pathogen.name)) {
                            pathCount ++;
                            pathCity = tempC;
                        }
                    }
                }                
            }
            if (pathCount == 1) {
                Object[] array = {tempP, pathCity};
                singleCityPathogen.add(array);
            }           
        }
        return singleCityPathogen;
    }
    
    public Pathogen getDeadlyPathogen(List<Object[]> pathogenCity ) {
        List<Pathogen> pathogen = new ArrayList<>();
        for (int i = 0; i < pathogenCity.size(); i++) {
	        Object[] array = pathogenCity.get(i);
	        pathogen.add((Pathogen) array[0]);
	    }
	    float killRating = 10;
	    Pathogen deadlyPath = null;
	    for (Pathogen temp : pathogen) {	        
	        float lethality = temp.lethality.level;
	        float duration = temp.duration.level;
	        float infectivity = temp.infectivity.level;
	        float mobility = temp.mobility.level;
	        
	        if (infectivity == -2) {
	            continue;
	        }
	        if (killRating == 10) {
	            killRating = lethality+mobility+duration/2;
	            deadlyPath = temp;
	            
	        } else {
	            if (killRating < lethality+mobility+duration/2 ) {
	                killRating = lethality+mobility+duration/2;
	                deadlyPath = temp;
	            }	                
	        }  
	    }
	    return deadlyPath;
    
    
    }
    
    public Pathogen getSlowPathogen(List<Pathogen> pathogen) {
    
	    float spreadRating = 10;
	    Pathogen slowPath = null;
	    
	    for(Pathogen temp : pathogen) {      
	        float duration = temp.duration.level;
	        float infectivity = temp.infectivity.level;
	        
	        if(infectivity == -2) {
	            continue;
	        }
	        if(spreadRating == 10) {
	            spreadRating = duration - infectivity;
	            slowPath = temp;
	            
	        } else {
	            if (spreadRating < duration - infectivity ) {
	                spreadRating = duration - infectivity;
	                slowPath = temp;
	            }	                
	        }  
	    }
	    return slowPath;
    
    
    }
    
    public List<Pathogen> acutePathCounter () { 
        List<Pathogen> acutePathogens = new ArrayList<>();
        List<Pathogen> pathogens = this.round.pathogens.values().stream()
            .collect(Collectors.toList());
        List<City> cities = this.round.cities.values().stream()
            .collect(Collectors.toList());

        for(Pathogen tempP : pathogens) {
            for(City tempC : cities) {
                City city = (City) tempC;
                List<BaseEvent> events = city.events;
                boolean acute = false;
                for(BaseEvent tempE : events) {
                    if (tempE.getClass().getSimpleName().equals("OutbreakEvent")){
                        OutbreakEvent event = (OutbreakEvent) tempE;
                        if (event.pathogen == tempP){
                            acute = true;                 
                        }                        
                    } else if (tempE.getClass().getSimpleName().equals("QuarantineEvent")){
                        acute = false; 
                        break;
                    }
                }
                if (acute == true) {
                    acutePathogens.add(tempP);
                    break;
                }
            }
        }
        return acutePathogens;
    }
    
    public boolean checkVaccDev(Pathogen path) {
        for(BaseEvent event : this.round.events) {
            if (event.getClass().getSimpleName().equals("VaccineInDevelopmentEvent")){
                VaccineInDevelopmentEvent eventV = (VaccineInDevelopmentEvent) event;
                if (eventV.pathogen == path){
                    return true;
                }
            }       
        }
        return false;    
    }
    public boolean checkVacc(Pathogen path) {
        for(BaseEvent event : this.round.events) {
            if (event.getClass().getSimpleName().equals("VaccineAvailableEvent")){
                VaccineAvailableEvent eventV = (VaccineAvailableEvent) event;
                if (eventV.pathogen == path){
                    return true;
                }
            }        
        }
        return false;
    }
    
    public boolean vaccineDeployedCheck(City city, Pathogen vaccPath) {
        for(BaseEvent tempE : city.events) {
	        if(tempE.getClass().getSimpleName().equals("VaccineDeployedEvent")) {
	            VaccineDeployedEvent deployEvent = (VaccineDeployedEvent) tempE;
	            if(vaccPath == deployEvent.pathogen) {
	                return true;
	            }
	        }	                
	    }
        return false;
    }
    
    public boolean cityInfectedCheck(City city, Pathogen vaccPath) {
        for(BaseEvent tempE : city.events) {
	        if(tempE.getClass().getSimpleName().equals("OutbreakEvent")) {
	            OutbreakEvent outbreakEvent = (OutbreakEvent) tempE;
	            if(vaccPath == outbreakEvent.pathogen) {
	                return true;
	            }
	        }	                
	    }
        return false;    
    }
    
        public boolean checkMedDev(Pathogen path) {
        for(BaseEvent event : this.round.events) {
            if (event.getClass().getSimpleName().equals("MedicationInDevelopmentEvent")){
                MedicationInDevelopmentEvent eventM = (MedicationInDevelopmentEvent) event;
                if (eventM.pathogen == path){
                    return true;
                }
            }       
        }
        return false;    
    }
    
    public boolean checkMed(Pathogen path) {
        for(BaseEvent event : this.round.events) {
            if (event.getClass().getSimpleName().equals("MedicationAvailableEvent")){
                MedicationAvailableEvent eventM = (MedicationAvailableEvent) event;
                if (eventM.pathogen == path){
                    return true;
                }
            }        
        }
        return false;
    }
    
    public boolean medicationDeployedCheck(City city, Pathogen vaccPath) {
        for(BaseEvent tempE : city.events) {
	        if(tempE.getClass().getSimpleName().equals("MedicationDeployedEvent")) {
	            MedicationDeployedEvent deployEvent = (MedicationDeployedEvent) tempE;
	            if(vaccPath == deployEvent.pathogen) {
	                return true;
	            }
	        }
	                
	    }
        return false;
    }
    
    public int findNewestCity(List<City> cities, Pathogen vaccPath) {
        int infectionRound = 0;
        for(City tempC : cities) {
            if(cityInfectedCheck(tempC, vaccPath) || vaccineDeployedCheck(tempC, vaccPath)) {
	            continue;
	        }	
            for(BaseEvent tempE : tempC.events) {
                if(tempE.getClass().getSimpleName().equals("OutbreakEvent")){
                    OutbreakEvent event = (OutbreakEvent) tempE;
                    if(event.sinceRound > infectionRound) {
                        infectionRound = event.sinceRound;
                    }
                }
            }
        }
        return infectionRound;
    }
    public List<City> getNewCities(int round, List<City> cities) {
        List<City> newCities = new ArrayList<>();
        for(City tempC : cities) {	        	
            for(BaseEvent tempE : tempC.events) {
                if(tempE.getClass().getSimpleName().equals("OutbreakEvent")){
                    OutbreakEvent event = (OutbreakEvent) tempE;
                    if(event.sinceRound == round) {
                        newCities.add(tempC);
                        break;                    
                    }
                }
            }
        }
        return newCities;
    }
}