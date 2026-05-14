import random
import csv

random.seed(42)

prenoms = [
    "Mohamed", "Ahmed", "Ali", "Omar", "Youssef", "Hamza", "Amine", "Bilel", "Walid", "Sami",
    "Karim", "Nizar", "Rami", "Tarek", "Khaled", "Maher", "Hedi", "Slim", "Fares", "Anis",
    "Bassem", "Chokri", "Dhia", "Emna", "Fatma", "Ghofrane", "Hajer", "Ines", "Jihen", "Khadija",
    "Latifa", "Mariem", "Nadia", "Olfa", "Rim", "Sarra", "Takwa", "Ulfet", "Vera", "Wafa",
    "Yasmine", "Zaineb", "Abir", "Bochra", "Cyrine", "Dorra", "Eya", "Feriel", "Ghada", "Hela",
    "Ichrak", "Jawher", "Kaouther", "Lamia", "Manel", "Nawal", "Oumaima", "Rania", "Sana", "Thouraya",
    "Asma", "Beya", "Chaima", "Dalel", "Elyes", "Farouk", "Ghassen", "Houssem", "Islem", "Jabeur",
    "Kais", "Lotfi", "Mehdi", "Nadhir", "Oussama", "Raed", "Sofiene", "Taoufik", "Uchef", "Vali",
    "Wassim", "Yassine", "Zied", "Adel", "Bader", "Chedly", "Dali", "Ezzedine", "Fethi", "Ghaieth",
    "Hatem", "Iheb", "Jilani", "Khalil", "Lassad", "Mounir", "Noureddine", "Osama", "Rachid", "Saber",
    "Taher", "Uthman", "Vissem", "Wissem", "Yahia", "Zoubeir", "Abdelaziz", "Bilal", "Chaker", "Driss",
    "Fethi", "Ghazi", "Habib", "Idris", "Jamel", "Kamel", "Lazhar", "Mondher", "Nacer", "Othman",
    "Radhouane", "Saddok", "Taissir", "Uthman", "Wahid", "Xavier", "Yamen", "Zarrouk", "Aymen", "Badis"
]

noms_famille = [
    "Ben Ali", "Trabelsi", "Chaabane", "Mansouri", "Hammami", "Gharbi", "Ayari", "Jebali", "Khalfi", "Meddeb",
    "Rekik", "Saidi", "Tlili", "Oueslati", "Nefzi", "Marzouki", "Ltifi", "Khelifi", "Jerbi", "Hamdi",
    "Guizani", "Ferchichi", "Elloumi", "Dhouib", "Chebbi", "Belhadj", "Amri", "Zouari", "Yahyaoui", "Weslati",
    "Turki", "Snoussi", "Riahi", "Qadri", "Piri", "Omrani", "Naceur", "Mejri", "Lassoued", "Karray",
    "Jouini", "Haddad", "Ghediri", "Ferjani", "Essid", "Dridi", "Chaouachi", "Bouzid", "Arfaoui", "Zghal",
    "Youssfi", "Xtouri", "Wahabi", "Vali", "Ustouri", "Tounsi", "Selmi", "Rouissi", "Qasmi", "Pouri",
    "Ouni", "Nouiri", "Mzoughi", "Letaief", "Ksentini", "Jlassi", "Ismail", "Hamza", "Ghorbali", "Fathalli",
    "Ezzaouia", "Daghfous", "Chaari", "Bouaziz", "Attia", "Zargouni", "Yazidi", "Xouri", "Wafi", "Tnani",
    "Slim", "Rhimi", "Qaidi", "Pouri", "Ouali", "Nouri", "Mkacher", "Louati", "Krichene", "Jaoua",
    "Ibrahimi", "Halouani", "Guesmi", "Farhat", "Ellouz", "Debbebi", "Chakroun", "Bsili", "Azaiez", "Zrida",
    "Yousfi", "Xtali", "Wafi", "Vouafi", "Touati", "Souissi", "Rjiba", "Qchouri", "Pouri", "Ouafi",
    "Nciri", "Mouelhi", "Lajmi", "Khouaja", "Jallouli", "Idriss", "Henia", "Gara", "Fennira", "Essafi",
    "Drissi", "Chouchane", "Boughanmi", "Agrebi", "Zaidi", "Yeddes", "Xouri", "Wardi", "Tebini", "Sbai"
]

sources_normal = ["LISTE_INTERNE"] * 9
sources_sanction = ["SANCTION"]

with open("data/noms.csv", "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerow(["id", "nom", "source"])
    id_ = 1
    while id_ <= 12000:
        prenom = random.choice(prenoms)
        famille = random.choice(noms_famille)
        nom = prenom + " " + famille
        source = random.choice(sources_normal + sources_sanction)
        writer.writerow([id_, nom, source])
        id_ += 1

print("Done: 12000 noms generes dans data/noms.csv")
