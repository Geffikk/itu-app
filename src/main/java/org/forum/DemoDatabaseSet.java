package org.forum.entities;

import org.forum.entities.user.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;


public class DemoDatabaseSet {

    public static void main(String[] args) {

        // create session factories
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Post.class)
                .addAnnotatedClass(Topic.class)
                .addAnnotatedClass(Section.class)
                .addAnnotatedClass(StudyYear.class)
                .addAnnotatedClass(Year.class)
                .addAnnotatedClass(UserAdditionalInfo.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();


        // create sessions

        Session session = factory.getCurrentSession();


        try {

            // create objects
            System.out.println("Creating new objects...");

            User tempUser1 = new User("patriktomov@gmail.com", "Patres", "$2y$12$M7fYQOMVRAuCK0bzrTqMiunl6BQv4IzmFGr6.5yYPE6okcgPTqfZS", 100, 10);
            User tempUser2 = new User("maros.geffert@gmail.com", "Maros", "$2y$12$M7fYQOMVRAuCK0bzrTqMiunl6BQv4IzmFGr6.5yYPE6okcgPTqfZS", 200, 20);
            User tempUser3 = new User("maros1.geffert@gmail.com", "Andrej", "$2y$12$M7fYQOMVRAuCK0bzrTqMiunl6BQv4IzmFGr6.5yYPE6okcgPTqfZS", 300, 30);
            User tempUser4 = new User("maros2.geffert@gmail.com", "Adam", "$2y$12$M7fYQOMVRAuCK0bzrTqMiunl6BQv4IzmFGr6.5yYPE6okcgPTqfZS", 400, 40);
            User tempUser5 = new User("maros3.geffert@gmail.com", "Tomas", "$2y$12$M7fYQOMVRAuCK0bzrTqMiunl6BQv4IzmFGr6.5yYPE6okcgPTqfZS", 500, 60);
            User tempUser6 = new User("maros4.geffert@gmail.com", "Martin", "$2y$12$M7fYQOMVRAuCK0bzrTqMiunl6BQv4IzmFGr6.5yYPE6okcgPTqfZS", 600, 50);


            UserAdditionalInfo tempInfo1 = new UserAdditionalInfo("0904481833", "Patrik", "Tomov", "Bardejov", "Student FIT", "VUT FIT");
            UserAdditionalInfo tempInfo2 = new UserAdditionalInfo("0904481833", "Maros", "Geffert", "Bardejov", "Student FIT", "VUT FIT");

            tempUser1.setInfo(tempInfo1);
            tempUser2.setInfo(tempInfo2);

            Year tempYear1 = new Year("2020");
            Year tempYear2 = new Year("2019");

            StudyYear tempStudyYear1 = new StudyYear("1BIT");
            StudyYear tempStudyYear2 = new StudyYear("2BIT");
            StudyYear tempStudyYear3 = new StudyYear("3BIT");
            StudyYear tempStudyYear4 = new StudyYear("VOLITELNE");
            StudyYear tempStudyYear5 = new StudyYear("VSEOBECNE");

            StudyYear tempStudyYear6 = new StudyYear("1BIT");
            StudyYear tempStudyYear7 = new StudyYear("2BIT");
            StudyYear tempStudyYear8 = new StudyYear("3BIT");
            StudyYear tempStudyYear9 = new StudyYear("VOLITELNE");
            StudyYear tempStudyYear10 = new StudyYear("VSEOBECNE");


            tempStudyYear1.setYear(tempYear1);
            tempStudyYear2.setYear(tempYear1);
            tempStudyYear3.setYear(tempYear1);
            tempStudyYear4.setYear(tempYear1);
            tempStudyYear5.setYear(tempYear1);

            tempStudyYear6.setYear(tempYear2);
            tempStudyYear7.setYear(tempYear2);
            tempStudyYear8.setYear(tempYear2);
            tempStudyYear9.setYear(tempYear2);
            tempStudyYear10.setYear(tempYear2);


            Section tempSkupina1 = new Section("IDA");
            Section tempSkupina2 = new Section("IEL");
            Section tempSkupina3 = new Section("IUS");
            Section tempSkupina4 = new Section("IZP");

            Section tempSkupina5 = new Section("IMA");
            Section tempSkupina6 = new Section("INC");
            Section tempSkupina7 = new Section("IOS");
            Section tempSkupina8 = new Section("ISU");


            Section tempSkupina9 = new Section("IDA");
            Section tempSkupina10 = new Section("IEL");
            Section tempSkupina11 = new Section("IUS");
            Section tempSkupina12 = new Section("IZP");

            Section tempSkupina13 = new Section("IMA");
            Section tempSkupina14 = new Section("INC");
            Section tempSkupina15 = new Section("IOS");
            Section tempSkupina16 = new Section("ISU");

            Topic tempTopic1 = new Topic("Skuska", "Viete co tam bude?");
            Topic tempTopic2 = new Topic("Polsemka", "Viete co tam bude?");
            Topic tempTopic3 = new Topic("Cvika", "Viete co tam bude?");
            Topic tempTopic4 = new Topic("Projekt", "Viete co tam bude?");
            Topic tempTopic5 = new Topic("Report", "Viete co tam bude?");
            Topic tempTopic6 = new Topic("Jakoo", "Viete co tam bude?");


            Topic tempTopic7 = new Topic("Skuska", "Viete co tam bude?");
            Topic tempTopic8 = new Topic("Polsemka", "Napady??");
            Topic tempTopic9 = new Topic("Cvika", "Prve cviko?");
            Topic tempTopic10 = new Topic("Projekt", "Viete co tam bude?");
            Topic tempTopic11 = new Topic("Report", "Viete co tam bude?");
            Topic tempTopic12 = new Topic("Jakoo", "Viete co tam bude?");


            tempUser1.addFavoriteTopic(tempTopic1);
            tempUser1.addFavoriteTopic(tempTopic2);
            tempUser1.addFavoriteTopic(tempTopic3);
            tempUser1.addFavoriteTopic(tempTopic4);

            Post tempPrispevok1 = new Post("Podľa mňa logaritmy.", 0);
            Post tempPrispevok2 = new Post("Čo som čítal tak by tam mali byť aj derivácie.", 0);
            Post tempPrispevok3 = new Post("Áno máte pravdu budu tam aj logaritmy aj derivácie.", 0);
            Post tempPrispevok4 = new Post("Asi nic.", 0);
            Post tempPrispevok5 = new Post("Mozno scitavanie.", 0);
            Post tempPrispevok6 = new Post("mozno aj nasobenie", 0);

            tempPrispevok1.setUser(tempUser1);
            tempPrispevok2.setUser(tempUser1);
            tempPrispevok3.setUser(tempUser1);
            tempPrispevok4.setUser(tempUser1);
            tempPrispevok5.setUser(tempUser1);
            tempPrispevok6.setUser(tempUser1);

            tempPrispevok1.setTopic(tempTopic1);
            tempPrispevok2.setTopic(tempTopic1);
            tempPrispevok3.setTopic(tempTopic1);
            tempPrispevok4.setTopic(tempTopic7);
            tempPrispevok5.setTopic(tempTopic7);
            tempPrispevok6.setTopic(tempTopic7);

            tempTopic1.setSection(tempSkupina1);
            tempTopic2.setSection(tempSkupina1);
            tempTopic3.setSection(tempSkupina1);
            tempTopic4.setSection(tempSkupina1);
            tempTopic5.setSection(tempSkupina1);
            tempTopic6.setSection(tempSkupina1);
            tempTopic7.setSection(tempSkupina9);
            tempTopic8.setSection(tempSkupina9);
            tempTopic9.setSection(tempSkupina9);
            tempTopic10.setSection(tempSkupina9);
            tempTopic11.setSection(tempSkupina9);
            tempTopic12.setSection(tempSkupina9);


            tempTopic1.setUser(tempUser1);
            tempTopic2.setUser(tempUser1);
            tempTopic3.setUser(tempUser1);
            tempTopic4.setUser(tempUser1);
            tempTopic5.setUser(tempUser1);
            tempTopic6.setUser(tempUser1);
            tempTopic7.setUser(tempUser1);
            tempTopic8.setUser(tempUser1);
            tempTopic9.setUser(tempUser1);
            tempTopic10.setUser(tempUser1);
            tempTopic11.setUser(tempUser1);
            tempTopic12.setUser(tempUser1);


            tempSkupina1.setStudyYear(tempStudyYear1);
            tempSkupina2.setStudyYear(tempStudyYear1);
            tempSkupina3.setStudyYear(tempStudyYear1);
            tempSkupina4.setStudyYear(tempStudyYear1);
            tempSkupina5.setStudyYear(tempStudyYear1);
            tempSkupina6.setStudyYear(tempStudyYear1);
            tempSkupina7.setStudyYear(tempStudyYear1);
            tempSkupina8.setStudyYear(tempStudyYear1);
            tempSkupina9.setStudyYear(tempStudyYear6);
            tempSkupina10.setStudyYear(tempStudyYear6);
            tempSkupina11.setStudyYear(tempStudyYear6);
            tempSkupina12.setStudyYear(tempStudyYear6);
            tempSkupina13.setStudyYear(tempStudyYear6);
            tempSkupina14.setStudyYear(tempStudyYear6);
            tempSkupina15.setStudyYear(tempStudyYear6);
            tempSkupina16.setStudyYear(tempStudyYear6);

            // start a transactions
            session.beginTransaction();


            // save objectss
            System.out.println("Saving objects...");

            session.save(tempUser1);
            session.save(tempUser2);
            session.save(tempUser3);
            session.save(tempUser4);
            session.save(tempUser5);
            session.save(tempUser6);

            session.save(tempYear1);
            session.save(tempYear2);

            session.save(tempStudyYear1);
            session.save(tempStudyYear2);
            session.save(tempStudyYear3);
            session.save(tempStudyYear4);
            session.save(tempStudyYear5);
            session.save(tempStudyYear6);
            session.save(tempStudyYear7);
            session.save(tempStudyYear8);
            session.save(tempStudyYear9);
            session.save(tempStudyYear10);


            session.save(tempSkupina1);
            session.save(tempSkupina2);
            session.save(tempSkupina3);
            session.save(tempSkupina4);
            session.save(tempSkupina5);
            session.save(tempSkupina6);
            session.save(tempSkupina7);
            session.save(tempSkupina9);
            session.save(tempSkupina10);
            session.save(tempSkupina11);
            session.save(tempSkupina12);
            session.save(tempSkupina13);
            session.save(tempSkupina14);
            session.save(tempSkupina15);
            session.save(tempSkupina16);


            session.save(tempTopic1);
            session.save(tempTopic2);
            session.save(tempTopic3);
            session.save(tempTopic4);
            session.save(tempTopic5);
            session.save(tempTopic6);
            session.save(tempTopic7);
            session.save(tempTopic8);
            session.save(tempTopic9);
            session.save(tempTopic10);
            session.save(tempTopic11);
            session.save(tempTopic12);

            session.save(tempPrispevok1);
            session.save(tempPrispevok2);
            session.save(tempPrispevok3);
            session.save(tempPrispevok4);
            session.save(tempPrispevok5);
            session.save(tempPrispevok6);

            // commit transaction
            session.getTransaction().commit();


            System.out.println("DONE!!!");
        }
        finally {
            factory.close();

        }
    }
}
