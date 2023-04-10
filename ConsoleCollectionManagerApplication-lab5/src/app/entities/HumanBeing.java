package app.entities;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@XmlRootElement(name = "humanBeing")
public class HumanBeing {

    private int id;


    private String name;


    private Coordinates coordinates;

    private java.time.LocalDateTime creationDate;


    private boolean realHero;
    private Boolean hasToothpick;

    private int impactSpeed;


    private String soundtrackName;


    private long minutesOfWaiting;


    private WeaponType weaponType;


    private Car car;

    public HumanBeing() {
        car = new Car();
        coordinates = new Coordinates();
        hasToothpick = false;
        name = "empty";
        weaponType = WeaponType.RIFLE;
    }


    @XmlAttribute
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    @XmlElement
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



    @XmlElement
    public Coordinates getCoordinates() {
        return coordinates;
    }


    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    @XmlElement
    @XmlJavaTypeAdapter(app.adapters.LocalDateTimeAdapter.class)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }


    @XmlElement
    public boolean isRealHero() {
        return realHero;
    }


    public void setRealHero(boolean realHero) {
        this.realHero = realHero;
    }


    @XmlElement
    public Boolean getHasToothpick() {
        return hasToothpick;
    }


    public void setHasToothpick(Boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }


    @XmlElement
    public int getImpactSpeed() {
        return impactSpeed;
    }


    public void setImpactSpeed(int impactSpeed) {
        this.impactSpeed = impactSpeed;
    }


    @XmlElement
    public String getSoundtrackName() {
        return soundtrackName;
    }


    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }


    @XmlElement
    public long getMinutesOfWaiting() {
        return minutesOfWaiting;
    }


    public void setMinutesOfWaiting(long minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }


    @XmlElement
    public WeaponType getWeaponType() {
        return weaponType;
    }


    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }


    @XmlElement
    public Car getCar() {
        return car;
    }


    public void setCar(Car car) {
        this.car = car;
    }


    @Override
    public String toString() {
        return String.format(
                "id: %d, name: %s, coordinates(x: %f, y: %d), creation date: %s, real hero: %s, has toothpick: %s, impact speed: %d, "
                        + "soundtrack name: %s, minutes of waiting: %d, weapon type: %s, car(name : %s)%n \n",
                id, name, coordinates.getX(), coordinates.getY(), creationDate.format(DateTimeFormatter.ISO_DATE_TIME), realHero,
                hasToothpick, impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car.getName());
    }


    public int compareTo(HumanBeing other) {
        return Integer.compare(this.id, other.id);
    }
}