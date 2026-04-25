package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.ImageBasicDTO;
import es.tickethub.tickethub.dto.SessionBasicDTO;
import es.tickethub.tickethub.dto.ZoneBasicDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ArtistRepository;
import es.tickethub.tickethub.repositories.DiscountRepository;
import es.tickethub.tickethub.repositories.ImageRepository;
import es.tickethub.tickethub.repositories.ZoneRepository;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMapper {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ImageRepository imageRepository;

    public Artist idToArtist(Long id) {
        if (id == null)
            return null;
        return artistRepository.findById(id).orElse(null);
    }

    public Discount idToDiscount(Long id) {
        if (id == null)
            return null;
        return discountRepository.findById(id).orElse(null);
    }

    public Zone toZoneEntity(ZoneBasicDTO dto) {
        if (dto == null)
            return null;
        Zone zone;
        if (dto.id() != null) {
            zone = zoneRepository.findById(dto.id()).orElse(new Zone());
        } else {
            zone = new Zone();
        }

        zone.setId(dto.id());
        zone.setName(dto.name());
        zone.setCapacity(dto.capacity());
        zone.setPrice(dto.price());
        return zone;
    }

    public Session toSessionEntity(SessionBasicDTO dto) {
        if (dto == null)
            return null;
        Session session = new Session();
        if (dto.sessionID() != null) {
            session.setSessionID(dto.sessionID());
        }
        session.setDate(dto.date());
        return session;
    }

    @AfterMapping
    public void linkSessions(@MappingTarget Event event) {
        if (event.getSessions() != null) {
            event.getSessions().forEach(session -> session.setEvent(event));
        }
    }

    public Image toImageEntity(ImageBasicDTO dto) {
        if (dto == null)
            return null;

        if (dto.imageID() != null) {
            return imageRepository.findById(dto.imageID()).orElse(null);
        }

        Image image = new Image();
        image.setImageName(dto.imageName());
        image.setFirst(dto.first());
        return image;
    }

    public ImageBasicDTO eventToMainImage(Event event) {
        if (event.getEventImages() == null || event.getEventImages().isEmpty()) {
            return null;
        }
        Image img = event.getEventImages().get(0);
        return new ImageBasicDTO(img.getImageID(), img.getImageName(), true);
    }
}
