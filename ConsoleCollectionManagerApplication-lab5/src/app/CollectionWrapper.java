package app;

import app.entities.HumanBeing;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "collection")
public class CollectionWrapper {
    private Set<HumanBeing> collection;
    private final LocalDateTime initializationDateTime;

    public CollectionWrapper() {
        collection = new HashSet<>();
        initializationDateTime = LocalDateTime.now();
    }


    @XmlElement(name = "humanBeing")
    public Collection<HumanBeing> getCollection() {
        return collection;
    }


    public void setCollection(Collection<HumanBeing> collection) {
        this.collection = (Set<HumanBeing>) collection;
    }


    public LocalDateTime getInitializationDateTime() {
        return initializationDateTime;
    }


    public void add(HumanBeing humanBeing) {
        int id = Math.abs(humanBeing.hashCode());
        humanBeing.setId(id);

        collection.add(humanBeing);
    }


    public void update(int id, HumanBeing humanBeing) {
        for(HumanBeing oldHumanBeing: collection) {
            if(oldHumanBeing.getId() == id) {
                humanBeing.setId(id);
                collection.remove(oldHumanBeing);
                collection.add(humanBeing);
                return;
            }
        }

    }


    public boolean removeById(int id) {
        for(HumanBeing humanBeing: collection) {
            if(humanBeing.getId() == id) {
                collection.remove(humanBeing);
                return true;
            }
        }

        return false;
    }

    
    public void clear() {
        collection.clear();
    }
}
