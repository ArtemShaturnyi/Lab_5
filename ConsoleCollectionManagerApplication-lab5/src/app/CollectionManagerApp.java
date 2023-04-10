package app;

import app.entities.Car;
import app.entities.Coordinates;
import app.entities.HumanBeing;
import app.entities.WeaponType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionManagerApp {
    private Scanner scanner;
    private CollectionWrapper wrapper;
    private List<String> commandHistory;


    public static CollectionWrapper readFromXMLFile(File file) throws IOException, JAXBException {
        InputStream inputStream = Files.newInputStream(file.toPath());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        JAXBContext jaxbContext = JAXBContext.newInstance(CollectionWrapper.class, HumanBeing.class);
        Unmarshaller xmlUnmarshaller = jaxbContext.createUnmarshaller();

        CollectionWrapper wrapper = (CollectionWrapper) xmlUnmarshaller.unmarshal(bufferedInputStream);

        inputStream.close();
        bufferedInputStream.close();

        return wrapper;
    }


    public static void saveToXMLFile(File file, Collection<HumanBeing> collection) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CollectionWrapper.class, HumanBeing.class);
        Marshaller xmlMarshaller = jaxbContext.createMarshaller();
        xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        CollectionWrapper wrapper = new CollectionWrapper();
        wrapper.setCollection(collection);

        OutputStream outputStream = Files.newOutputStream(file.toPath());
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        xmlMarshaller.marshal(wrapper, bufferedWriter);

        outputStream.close();
        bufferedWriter.close();
    }


    public void run() {
        scanner = new Scanner(System.in);
        commandHistory = new ArrayList<>();

        while(true) {
            System.out.print("> ");

            String command = scanner.nextLine();
            String[] commandParts = command.trim().split(" ");

            if(commandHistory.size() >= 5) {
                commandHistory.remove(0);
            }

            commandHistory.add(commandParts[0]);
            analyzeCommand(commandParts);
        }
    }


    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Не введен файл.");
            return;
        }

        CollectionManagerApp app = new CollectionManagerApp();
        try {
            app.wrapper = readFromXMLFile(new File(args[0]));
        } catch (IOException| JAXBException e) {
            System.out.println("Ошибка открытия или парсинга файла.");
            return;
        }

        app.run();
    }


    private void analyzeCommand(String[] commandParts) {
        switch(commandParts[0]) {
            case "exit":
                System.out.println("EXIT");
                System.exit(0);
                return;
            case "help":
                help();
                break;
            case "info":
                info();
                break;
            case "show":
                show();
                break;
            case "add":
                add(0);
                break;
            case "remove_by_id":
                if(commandParts.length > 1) {
                    removeById(commandParts[1]);
                } else {
                    System.out.println("Не введен параметр");
                }
                break;
            case "update":
                if(commandParts.length > 1) {
                    update(commandParts[1]);
                } else {
                    System.out.println("Не введен параметр");
                }
                break;
            case "clear":
                wrapper.clear();
                break;
            case "save":
                save();
                break;
            case "history":
                history();
                break;
            case "remove_greater":
                removeGreater();
                break;
            case "remove_lower":
                removeLower();
                break;
            case "count_by_car":
                countByCar();
                break;
            case "print_ascending":
                printAscending();
                break;
            case "print_unique_car":
                printUniqueCar();
                break;
            case "execute_script":
                if(commandParts.length > 1) {
                    executeScript(commandParts[1]);
                } else {
                    System.out.println("Не введен параметр");
                }
                break;
            default:
                System.out.println("Неизвестная команда");
                break;
        }
    }


    private void help() {
        System.out.println("•	help : вывести справку по доступным командам\n"+
                "•	info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.\n" +
                "•	show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "•	add {element} : добавить новый элемент в коллекцию\n" +
                "•	update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "•	remove_by_id id : удалить элемент из коллекции по его id\n" +
                "•	clear : очистить коллекцию\n" +
                "•	save : сохранить коллекцию в файл\n" +
                "•	execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "•	exit : завершить программу (без сохранения в файл)\n" +
                "•	remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "•	remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "•	history : вывести последние 5 команд (без их аргументов)\n" +
                "•	count_by_car car : вывести количество элементов, значение поля car которых равно заданному\n" +
                "•	print_ascending : вывести элементы коллекции в порядке возрастания\n" +
                "•	print_unique_car : вывести уникальные значения поля car всех элементов в коллекции");
    }


    private void info() {
        System.out.println("Collection: \n"
                + " Тип: " + wrapper.getCollection().getClass().toString()
                + "\n Кол-во элементов: " + wrapper.getCollection().size()
                + "\n Дата инициализации: " + wrapper.getInitializationDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    private void show() {
        for(HumanBeing human: wrapper.getCollection()) {
            Coordinates coords = human.getCoordinates();

            System.out.printf(
                    "id: %d, name: %s, coordinates(x: %f, y: %d), creation date: %s, real hero: %s, has toothpick: %s, impact speed: %d, "
                            + "soundtrack name: %s, minutes of waiting: %d, weapon type: %s, car(name : %s)%n \n",
                    human.getId(), human.getName(), coords.getX(), coords.getY(), human.getCreationDate().format(DateTimeFormatter.ISO_DATE),
                    human.isRealHero(), human.getHasToothpick(), human.getImpactSpeed(), human.getSoundtrackName(),
                    human.getMinutesOfWaiting(), human.getWeaponType(), human.getCar().getName()
            );
        }
    }


    private HumanBeing add(int id) {
        HumanBeing human = new HumanBeing();

        while(true) {
            System.out.print("Введите имя: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Имя не может быть пустым.");
                continue;
            }
            human.setName(name);
            break;
        }


        Coordinates coordinates = new Coordinates();

        while(true) {
            System.out.print("Введите координату x: ");
            try {
                double x = Double.parseDouble(scanner.nextLine());
                if (x <= -125) {
                    System.out.println("Значение координаты x должно быть больше -125.");
                    continue;
                }
                coordinates.setX(x);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат числа для координаты x.");
            }
        }

        while (true) {
            System.out.print("Введите координату y: ");
            try {
                long y = Long.parseLong(scanner.nextLine());
                coordinates.setY(y);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат числа для координаты y.");
            }
        }

        human.setCoordinates(coordinates);
        human.setCreationDate(LocalDateTime.now());

        while(true) {
            System.out.print("Является ли героем настоящим (true/false): ");
            try {
                boolean realHero = Boolean.parseBoolean(scanner.nextLine());
                human.setRealHero(realHero);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат для значения поля realHero.");
            }
        }

        while(true) {
            System.out.print("Есть ли у персонажа зубочистка (true/false): ");
            try {
                Boolean hasToothpick = Boolean.parseBoolean(scanner.nextLine());
                human.setHasToothpick(hasToothpick);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат для значения поля hasToothpick.");
            }
        }

        while(true) {
            System.out.print("Введите скорость удара: ");
            try {
                int impactSpeed = Integer.parseInt(scanner.nextLine());
                human.setImpactSpeed(impactSpeed);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат числа для скорости удара.");
            }
        }

        while(true) {
            System.out.print("Введите название саундтрека: ");
            String soundtrackName = scanner.nextLine().trim();
            if (soundtrackName.isEmpty()) {
                System.out.println("Название саундтрека не может быть пустым.");
                continue;
            }

            human.setSoundtrackName(soundtrackName);
            break;
        }

        while(true) {
            System.out.print("Введите время ожидания: ");
            try {
                human.setMinutesOfWaiting(Long.parseLong(scanner.nextLine().trim()));
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат числа для время ожидания.");
            }
        }

        while(true) {
            System.out.print("Введите тип оружия(RIFLE/SHOTGUN/MACHINE_GUN): ");
            String weaponTypeStr = scanner.nextLine();

            try {
                WeaponType weaponType = WeaponType.valueOf(weaponTypeStr);
                human.setWeaponType(weaponType);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректное название оружия.");
            }
        }

        System.out.print("Введите название машины: ");
        String carName = scanner.nextLine();

        human.setCar(new Car(carName));

        if(id == 0) {
            wrapper.add(human);
            System.out.println("Добавлен");
        }

        return human;
    }

    private void update(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            HumanBeing humanBeing = add(-1);

            wrapper.update(id, humanBeing);
            System.out.println("Обновлен");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат id.");
        }

    }


    private void removeById(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if(!wrapper.removeById(id)) {
                System.out.println("Элемент с таким id не найден.");
                return;
            }
            System.out.println("Элемент удалён.");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат id.");
        }
    }


    private void save() {
        System.out.print("Введите название файла: ");
        String fileName = scanner.nextLine();

        try {
            saveToXMLFile(new File(fileName), wrapper.getCollection());
        } catch (Exception e) {
            System.out.println("Невозможно сохранить файл.");
            return;
        }

        System.out.println("Коллекция сохранена");
    }


    private void history() {
        System.out.println("Последние 5 команд: ");
        for(String history: commandHistory) {
            System.out.println(history);
        }
    }


    private void removeGreater() {
        HumanBeing humanBeing = add(-1);

        for (HumanBeing humanBeing1 : wrapper.getCollection()) {
            if (humanBeing.compareTo(humanBeing1) > 0) {
                wrapper.removeById(humanBeing1.getId());
            }
        }
    }


    private void removeLower() {
        HumanBeing humanBeing = add(-1);

        for (HumanBeing humanBeing1 : wrapper.getCollection()) {
            if (humanBeing.compareTo(humanBeing1) < 0) {
                wrapper.removeById(humanBeing1.getId());
            }
        }
    }


    private void countByCar() {
        System.out.print("Введите название машины: ");
        String name = scanner.nextLine();

        wrapper.getCollection().forEach((human) -> {
            String carName = human.getCar().getName();

            if(carName != null && carName.equals(name)) {
                System.out.println(human);
            }
        });
    }


    private void printAscending() {
        List<HumanBeing> sortedList = new ArrayList<>(wrapper.getCollection());
        sortedList.sort(HumanBeing::compareTo);

        sortedList.forEach(System.out::println);
    }

    private void printUniqueCar() {
        List<String> uniqueCarNames = wrapper.getCollection().stream()
                .map(HumanBeing::getCar)
                .map(Car::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("Уникальные имена машин:");

        List<String> copies = new ArrayList<>();
        for(int i = 0; i < uniqueCarNames.size(); i++) {
            for(int j = 0; j < uniqueCarNames.size(); j++) {
                if(i != j && uniqueCarNames.get(i).equals(uniqueCarNames.get(j))) {
                    copies.add(uniqueCarNames.get(i));
                }
            }
        }

        for (String copy : copies) {
            uniqueCarNames.removeIf(s -> s.equals(copy));
        }

        uniqueCarNames.forEach(System.out::println);
    }


    private void executeScript(String fileName) {
        Scanner fileScanner;

        try {
            fileScanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Невозможно открыть файл.");
            return;
        }

        while(fileScanner.hasNext()) {
            String[] commandParts = fileScanner.nextLine().trim().split(" ");
            analyzeCommand(commandParts);
        }
    }
}
