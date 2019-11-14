package de.tubs.pandemieinc.events;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class EventFactory {

	// Pathogen name, Pathogen class (to avoid creating unnessary Pathogen classes)
    public Map<String, Pathogen> pathogens;
    public Map<String, City> cities;
    public Map<String, Function<JsonNode, BaseEvent>> eventParserMapping;

    public EventFactory(Map<String, City> cities) {
        this.cities = cities;
        this.pathogens = new HashMap<String, Pathogen>();

        this.eventParserMapping = new HashMap<String, Function<JsonNode, BaseEvent>>();

        this.eventParserMapping.put(AntiVaccinationismEvent.eventName,
                                    node -> this.parseFromSimpleEvent(node, AntiVaccinationismEvent::new));
        this.eventParserMapping.put(AirportClosedEvent.eventName, this::parseAirportClosedEvent);
        this.eventParserMapping.put(BioTerrorismEvent.eventName, this::parseBioTerrorismEvent);
        this.eventParserMapping.put(CampaignLaunchedEvent.eventName, this::parseCampaignLaunchedEvent);
        this.eventParserMapping.put(ConnectionClosedEvent.eventName, this::parseConnectionClosedEvent);
        this.eventParserMapping.put(EconomicCrisisEvent.eventName,
                                    node -> this.parseFromSimpleEvent(node, EconomicCrisisEvent::new));
        this.eventParserMapping.put(ElectionsCalledEvent.eventName, this::parseElectionsCalledEvent);
        this.eventParserMapping.put(HygienicMeasuresAppliedEvent.eventName,
                                    this::parseHygienicMeasuresAppliedEvent);
        this.eventParserMapping.put(InfluenceExertedEvent.eventName, this::parseInfluenceExertedEvent);
        this.eventParserMapping.put(LargeScalePanicEvent.eventName,
                                    node -> this.parseFromSimpleEvent(node, LargeScalePanicEvent::new));
        this.eventParserMapping.put(MedicationAvailableEvent.eventName,
                                    node -> this.parsePandemieEvent(node, MedicationAvailableEvent::new));
        this.eventParserMapping.put(MedicationDeployedEvent.eventName,
                                    node -> this.parseMedicationDeployedEvent(node));
        this.eventParserMapping.put(MedicationInDevelopmentEvent.eventName,
                                    node -> this.parseInDevelopmentEvent(node, MedicationInDevelopmentEvent::new));
        this.eventParserMapping.put(OutbreakEvent.eventName, this::parseOutbreakEvent);
        this.eventParserMapping.put(PathogenEncounteredEvent.eventName,
                                    this::parsePathogenEncounteredEvent);
        this.eventParserMapping.put(QuarantineEvent.eventName, this::parseQuarantineEvent);
        this.eventParserMapping.put(UprisingEvent.eventName, this::parseUprisingEvent);
        this.eventParserMapping.put(VaccineAvailableEvent.eventName,
                                    node -> this.parsePandemieEvent(node, VaccineAvailableEvent::new));
        this.eventParserMapping.put(VaccineDeployedEvent.eventName,
                                    node -> this.parseVaccineDeployedEvent(node));
        this.eventParserMapping.put(VaccineInDevelopmentEvent.eventName,
                                    node -> this.parseInDevelopmentEvent(node, VaccineInDevelopmentEvent::new));
    }

	public BaseEvent parseFromJsonNode(JsonNode node) {
		// Parse the event type
		JsonNode eventType = node.get("type");
		if (eventType == null) {
			return null;
		}

		// Try to parse the event from the know ones
		String type = eventType.textValue();
		Function<JsonNode, BaseEvent> parseFun = this.eventParserMapping.get(type);
		if (parseFun != null) {
			return parseFun.apply(node);
		}

		return this.parseOtherEvent(node);
	}

	public OtherEvent parseOtherEvent(JsonNode node) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> fields = mapper.convertValue(node, new TypeReference<HashMap<String, Object>>(){});
		try {
    		String debug = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fields);
        System.out.println(debug);
        } catch (Exception e) {
            System.out.println("Nope");
            System.out.println(e);
        }
		return new OtherEvent(fields);
	}

	public BaseEvent parseFromSimpleEvent(JsonNode node, Function<Integer, BaseEvent> constructor) {
		JsonNode value = node.path("sinceRound");
		if (!value.isInt()) {
			return null;
		}
		BaseEvent event = constructor.apply(value.asInt());
		return event;
	}

	public PathogenEncounteredEvent parsePathogenEncounteredEvent(JsonNode node) {
		// Parse the Pathogen
		JsonNode value = node.path("pathogen").path("name");
		if (value.isMissingNode()) {
			return null;
		}

		Pathogen pathogen = this.pathogens.get(value.textValue());
		if (pathogen == null) {
			pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
			if (pathogen == null) {
				return null;
			}
			this.pathogens.put(pathogen.name, pathogen);
		}

		value = node.path("round");
		if (!value.isInt()) {
			return null;
		}
		return new PathogenEncounteredEvent(value.asInt(), pathogen);
	}

	public BioTerrorismEvent parseBioTerrorismEvent(JsonNode node) {
		// Parse the Pathogen
		JsonNode value = node.path("pathogen").path("name");
		if (value.isMissingNode()) {
			return null;
		}

		Pathogen pathogen = this.pathogens.get(value.textValue());
		if (pathogen == null) {
			pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
			if (pathogen == null) {
				return null;
			}
			this.pathogens.put(pathogen.name, pathogen);
		}

		value = node.path("round");
		if (!value.isInt()) {
			return null;
		}
		return new BioTerrorismEvent(pathogen, value.asInt());
	}

	public OutbreakEvent parseOutbreakEvent(JsonNode node) {
		// Parse the Pathogen
		JsonNode value = node.path("pathogen").path("name");
		if (value.isMissingNode()) {
			return null;
		}
		// Try to generate the Pathogen instance)
		Pathogen pathogen = this.pathogens.get(value.textValue());
		if (pathogen == null) {
			pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
			if (pathogen == null) {
				return null;
			}
			this.pathogens.put(pathogen.name, pathogen);
		}

		// Parse sinceRound
		value = node.path("sinceRound");
		if (!value.isInt()) {
			return null;
		}
		int sinceRound = value.asInt();

		// Parse prevalence
		value = node.path("prevalence");
		if (!value.isDouble()) {
			return null;
		}
		double prevalence = value.asDouble();
		return new OutbreakEvent(pathogen, prevalence, sinceRound);
	}

    public BaseEvent parsePandemieEvent(JsonNode node, Supplier<PathogenEvent> constructor) {
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to generate the Pathogen instance)
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }

        // Parse sinceRound
        value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();
        PathogenEvent event = constructor.get();

        event.pathogen = pathogen;
        event.setRound(sinceRound);
        return event;
    }

    public BaseEvent parseVaccineDeployedEvent(JsonNode node) {
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to generate the Pathogen instance)
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }

        // Parse sinceRound
        value = node.path("round");
        if (!value.isInt()) {
            return null;
        }
        int round = value.asInt();
        VaccineDeployedEvent event = new VaccineDeployedEvent(pathogen, round);
        return event;
    }

    public BaseEvent parseMedicationDeployedEvent(JsonNode node) {
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to generate the Pathogen instance)
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }

        // Parse sinceRound
        value = node.path("round");
        if (!value.isInt()) {
            return null;
        }
        int round = value.asInt();
        MedicationDeployedEvent event = new MedicationDeployedEvent(pathogen, round);
        return event;
    }

    public BaseEvent parseQuarantineEvent(JsonNode node) {
        // Parse sinceRound
        JsonNode value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        QuarantineEvent event = new QuarantineEvent();
        event.untilRound = untilRound;
        event.sinceRound = sinceRound;
        return event;
    }

    public BaseEvent parseAirportClosedEvent(JsonNode node) {
        // Parse sinceRound
        JsonNode value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        AirportClosedEvent event = new AirportClosedEvent();
        event.untilRound = untilRound;
        event.sinceRound = sinceRound;
        return event;
    }

    public BaseEvent parseConnectionClosedEvent(JsonNode node) {
        JsonNode value = node.get("city");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to generate the City instance)
        City city = this.cities.get(value.textValue());
        if (city == null) {
            System.out.println("NEIN NEIN NEIN");
            return null;
        }

        // Parse sinceRound
        value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        ConnectionClosedEvent event = new ConnectionClosedEvent();
        event.city = city;
        event.untilRound = untilRound;
        event.sinceRound = sinceRound;
        return event;
    }

    public BaseEvent parseInDevelopmentEvent(JsonNode node, Supplier<InDevelopmentEvent> constructor) {
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to generate the Pathogen instance)
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }

        // Parse sinceRound
        value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        InDevelopmentEvent event = constructor.get();
        event.pathogen = pathogen;
        event.untilRound = untilRound;
        event.setRound(sinceRound);
        return event;
    }

    public BaseEvent parseUprisingEvent(JsonNode node) {
        // Parse sinceRound
        JsonNode value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("participants");
        if (!value.isInt()) {
            return null;
        }
        int participants = value.asInt();

        UprisingEvent event = new UprisingEvent(sinceRound, participants);
        return event;
    }

    public BaseEvent parseCampaignLaunchedEvent(JsonNode node) {
        JsonNode value = node.path("city").path("name");

        // Parse round
        value = node.path("round");
        if (!value.isInt()) {
            return null;
        }
        int round = value.asInt();

        CampaignLaunchedEvent event = new CampaignLaunchedEvent(round);
        return event;
    }

    public BaseEvent parseElectionsCalledEvent(JsonNode node) {
        JsonNode value = node.path("city").path("name");

        // Parse round
        value = node.path("round");
        if (!value.isInt()) {
            return null;
        }
        int round = value.asInt();

        ElectionsCalledEvent event = new ElectionsCalledEvent(round);
        return event;
    }

    public BaseEvent parseHygienicMeasuresAppliedEvent(JsonNode node) {
        JsonNode value = node.path("city").path("name");

        // Parse round
        value = node.path("round");
        if (!value.isInt()) {
            return null;
        }
        int round = value.asInt();

        HygienicMeasuresAppliedEvent event = new HygienicMeasuresAppliedEvent(round);
        return event;
    }

    public BaseEvent parseInfluenceExertedEvent(JsonNode node) {
        JsonNode value = node.path("city").path("name");

        // Parse round
        value = node.path("round");
        if (!value.isInt()) {
            return null;
        }
        int round = value.asInt();

        InfluenceExertedEvent event = new InfluenceExertedEvent(round);
        return event;
    }
}
