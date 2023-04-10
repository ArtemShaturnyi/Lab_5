package app.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "car")
public class Car {

    @XmlElement
    private String name;

    public Car(String name) {
        this.name = name;
    }
    public Car() {}
    public String getName() {
        return name;
    }
}