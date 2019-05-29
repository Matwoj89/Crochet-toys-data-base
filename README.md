## Crochet-toys-data-base
Java project created in Eclipse at 29.05.2019.

Technology used in project:
- Java SE
- XAMPP 7.3.5
- Apache server 2.4.39
- MySQL
- Eclipse IDE

Project was created to watch, save and update crochet toys made by Monika.

For proper connection user have to launch Apache server, and MySQL module in XAMPP.
All files are saved on http://localhost/phpmyadmin.

User is able to use few panels:
1. "What is it?" Text area + scroll pane with explementation what is "Amigurumi"
2. Adding new crochet toys panel
    - Active button "Choose image" allow to select image loaded on image field
    - Name, Price - text area for said values
    - Date - using DateChooser.jar as a calendar
    - Active button "Insert" add choosen data to data base
3. Image panel
    - Image Label implements resizelmage method which provides users with adaptable picture size
    - Active button First - shows first image in data base
    - Active button Previous - shows previous image in data base
    - ID - actual position ID
    - Active button Next - shows next image in data base
    - Active button Last - shows last image in data base
    - Active button Update - allow user to edit an actual data
    - Active button Delete - remove actual data from data base

User is able to see all toys saved in table above.
Every toy has its unique ID, name, price and date. 

![GUI_Amigurumi_Data_Base](https://user-images.githubusercontent.com/5953716/58577625-2cf42f00-8247-11e9-8f25-78bd621b2c60.jpg)
