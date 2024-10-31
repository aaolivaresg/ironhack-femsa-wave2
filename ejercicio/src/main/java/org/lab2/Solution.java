package org.lab2;

/**
 *
 * Task 1: Analyze the Code
 *
 * Objective: Review the provided code and identify where it violates any of the SOLID principles. Consider aspects like class responsibilities, method functionalities, dependency management, and object substitution.
 *
 * SRP: No tiene una única responsabilidad
 * OCP: hay que modificar la clase para agregar nuevas funcionalidades
 * ISP: Se puede mejorar la estructura de la clase para que no dependa de métodos que no debería usar
 *
 * El LSP y el DIP no se violan en este caso, ya que la clase no está haciendo uso de interfaces por lo tanto no podríamos decir que se está violando el principio de inversión de dependencias.
 * o el principio de sustitución de Liskov, ya que no hay herencia en este caso.
 *
 *
 * Task 2: Refactor the Code
 * Objective: Modify the code to address the identified violations. Ensure each component or class adheres more closely to the SOLID principles. Aim to enhance the system’s overall design in terms of scalability, flexibility, and maintainability.
 *
 *
 * Task 3: Document Your Changes
 * Objective: For each modification you make, provide a detailed explanation of:
 * What the issue was.
 * Which SOLID principle it violated.
 * How your change addresses this violation.
 * The benefits your changes bring to the system’s architecture.
 *
 *
 * 1.- Se descompuso la clase para que cada funcionalidad esté en un clase diferente, esto para que cada clase tenga una única responsabilidad.
 * Con este cambio cumplimos con el principio de responsabilidad única.
 *
 * 2.- Se crearon clases concretas para cada tipo de pago, esto para que si en un futuro se quiere agregar un nuevo tipo de pago, no se tenga que modificar la clase PaymentProcessor.
 * Esto cumple con el principio de abierto/cerrado.
 *
 * 3.- El principio de listov se cumple debido a que podemos sutituir la clase PaymentProcessor por cualquier clase que implemente la interfaz.
 * Incluso la inteface Notification se puede sutituir por SmsNotification o EmailNotification.
 *
 * 4.- Se eliminaron los métodos que no se estaban utilizando, esto para que la clase no dependa de métodos que no debería usar.
 * Esto cumple con el principio de segregación de interfaces.
 *
 * 5.- Se inyectaron las dependencias en el constructor de la clase OrderManager, esto para que la clase no dependa de clases concretas, sino de interfaces.
 * Esto cumple con el principio de inversión de dependencias.
 *
**/

public class Solution {
    public static void main(String[] args) {
        Order order = new Order();
        PaymentProcessor standardPaymentProcessor = new StandardPaymentProcessor();
        PaymentProcessor expressPaymentProcessor = new ExpressPaymentProcessor();
        OrderRepository orderRepository = new OrderRepositoryImpl();
        Notification smsNotification = new SmsNotification();
        Notification emailNotification = new EmailNotification();
        InventoryProcessor inventoryProcessor = new InventoryProcessorImpl();
        OrderManager orderManager = new OrderManager(standardPaymentProcessor, orderRepository, emailNotification, inventoryProcessor);
        OrderManager orderManager2 = new OrderManager(expressPaymentProcessor, orderRepository, smsNotification, inventoryProcessor);

        try {
            orderManager.processOrder(order);
            System.out.println("*******************");
            orderManager2.processOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Order{

}

interface PaymentProcessor {
    void processPayment(Order order);
}

class StandardPaymentProcessor implements PaymentProcessor {


    @Override
    public void processPayment(Order order) {
        System.out.println("processing standar payment");
    }
}

class ExpressPaymentProcessor implements PaymentProcessor {


    @Override
    public void processPayment(Order order) {
        System.out.println("processing express payment");
    }
}

interface OrderRepository {
    void update(Order order);
}


class OrderRepositoryImpl implements OrderRepository {


    @Override
    public void update(Order order) {
        System.out.println("updating order");
    }
}


interface Notification {
    void notifyCustomer(String message);
}


class EmailNotification implements Notification {


    @Override
    public void notifyCustomer(String message) {
        System.out.println("sending email:"+message);
    }
}

class SmsNotification implements Notification {


    @Override
    public void notifyCustomer(String message) {
        System.out.println("sending sms:"+message);
    }
}

interface InventoryProcessor {
    void verify();
}

class InventoryProcessorImpl implements InventoryProcessor {

    @Override
    public void verify() {
        System.out.println("verifying inventory");
    }
}

class OrderManager {
    private PaymentProcessor paymentProcessor;
    private OrderRepository orderRepository;
    private Notification notification;
    private InventoryProcessor inventoryProcessor;

    public OrderManager(PaymentProcessor paymentProcessor,
                        OrderRepository orderRepository,
                        Notification notification,
                        InventoryProcessor inventoryProcessor) {
        this.paymentProcessor = paymentProcessor;
        this.orderRepository = orderRepository;
        this.notification = notification;
        this.inventoryProcessor = inventoryProcessor;
    }

    public void processOrder(Order order){
        inventoryProcessor.verify();
        paymentProcessor.processPayment(order);
        orderRepository.update(order);
        notification.notifyCustomer("Order processed");
    }
}
