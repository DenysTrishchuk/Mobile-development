
import Foundation

// Частина 1

// Дано рядок у форматі "Student1 - Group1; Student2 - Group2; ..."

let studentsStr = "Дмитренко Олександр - ІП-84; Матвійчук Андрій - ІВ-83; Лесик Сергій - ІО-82; Ткаченко Ярослав - ІВ-83; Аверкова Анастасія - ІО-83; Соловйов Даніїл - ІО-83; Рахуба Вероніка - ІО-81; Кочерук Давид - ІВ-83; Лихацька Юлія - ІВ-82; Головенець Руслан - ІВ-83; Ющенко Андрій - ІО-82; Мінченко Володимир - ІП-83; Мартинюк Назар - ІО-82; Базова Лідія - ІВ-81; Снігурець Олег - ІВ-81; Роман Олександр - ІО-82; Дудка Максим - ІО-81; Кулініч Віталій - ІВ-81; Жуков Михайло - ІП-83; Грабко Михайло - ІВ-81; Іванов Володимир - ІО-81; Востриков Нікіта - ІО-82; Бондаренко Максим - ІВ-83; Скрипченко Володимир - ІВ-82; Кобук Назар - ІО-81; Дровнін Павло - ІВ-83; Тарасенко Юлія - ІО-82; Дрозд Світлана - ІВ-81; Фещенко Кирил - ІО-82; Крамар Віктор - ІО-83; Іванов Дмитро - ІВ-82"

// Завдання 1
// Заповніть словник, де:
// - ключ – назва групи
// - значення – відсортований масив студентів, які відносяться до відповідної групи

var studentsGroups: [String: [String]] = [:]

// Ваш код починається тут
for line in studentsStr.components(separatedBy: "; ") {
    let studentInfo: [String] = line.components(separatedBy: " - ")
    studentsGroups[studentInfo[1], default: []].append(studentInfo[0])
}
studentsGroups = studentsGroups.mapValues{$0.sorted()}
// Ваш код закінчується тут

print("Завдання 1")
print(studentsGroups)
print()

// Дано масив з максимально можливими оцінками

let points: [Int] = [12, 12, 12, 12, 12, 12, 12, 16]

// Завдання 2
// Заповніть словник, де:
// - ключ – назва групи
// - значення – словник, де:
//   - ключ – студент, який відносяться до відповідної групи
//   - значення – масив з оцінками студента (заповніть масив випадковими значеннями, використовуючи функцію `randomValue(maxValue: Int) -> Int`)

func randomValue(maxValue: Int) -> Int {
    switch(arc4random_uniform(6)) {
    case 1:
        return Int(ceil(Float(maxValue) * 0.7))
    case 2:
        return Int(ceil(Float(maxValue) * 0.9))
    case 3, 4, 5:
        return maxValue
    default:
        return 0
    }
}

var studentPoints: [String: [String: [Int]]] = [:]

// Ваш код починається тут
for (group, students) in studentsGroups {
    for student in students {
        var list: [Int] = []
        for point in points {
            list.append(randomValue(maxValue: point))
        }
        studentPoints[group, default: [:]][student] = list
    }
}
// Ваш код закінчується тут

print("Завдання 2")
print(studentPoints)
print()

// Завдання 3
// Заповніть словник, де:
// - ключ – назва групи
// - значення – словник, де:
//   - ключ – студент, який відносяться до відповідної групи
//   - значення – сума оцінок студента

var sumPoints: [String: [String: Int]] = [:]

// Ваш код починається тут
for (group, students) in studentPoints {
    for student in students.keys {
        sumPoints[group, default: [:]][student] = students[student]!.reduce(0, +)
    }
}
// Ваш код закінчується тут

print("Завдання 3")
print(sumPoints)
print()

// Завдання 4
// Заповніть словник, де:
// - ключ – назва групи
// - значення – середня оцінка всіх студентів групи

var groupAvg: [String: Float] = [:]

// Ваш код починається тут
for (group, students) in sumPoints {
    groupAvg[group] = Float(students.values.reduce(0, +)) / Float(students.count)
}


// Ваш код закінчується тут

print("Завдання 4")
print(groupAvg)
print()

// Завдання 5
// Заповніть словник, де:
// - ключ – назва групи
// - значення – масив студентів, які мають >= 60 балів

var passedPerGroup: [String: [String]] = [:]

// Ваш код починається тут
for (group, students) in sumPoints {
    for student in students.keys {
        if students[student]! >= 60 {
            passedPerGroup[group, default: []].append(student)
        }
    }
}
// Ваш код закінчується тут

print("Завдання 5")
print(passedPerGroup)


enum Direction {
    case latitude, longitude
}

class CoordinateDT {
    var direction: Direction
    var degrees: Int
    var minutes, seconds: UInt
    
    init() {
        self.direction = Direction.latitude
        self.degrees = 0
        self.minutes = 0
        self.seconds = 0
    }
    
    init(direction: Direction, degrees: Int, minutes: UInt, seconds: UInt) {
        self.direction = direction
        self.degrees = ((direction == Direction.latitude && (degrees >= -90 && degrees <= 90)) || (direction == Direction.longitude && (degrees >= -180 && degrees <= 180))) ? degrees : 0
        self.minutes = (minutes >= 0 && minutes <= 59) ? minutes : 0
        self.seconds = (seconds >= 0 && seconds <= 59) ? seconds : 0
    }
    
    func string() -> String {
        let direction: Character
        if self.direction == Direction.latitude {
            direction = (self.degrees < 0) ? "S" : "N"
        } else {
            direction = (self.degrees < 0) ? "W" : "E"
        }
        return "\(self.degrees)°​​\(self.minutes)′​\(self.seconds)​” \(direction)"
    }
    
    func decimalString() -> String {
        let direction: Character
        if self.direction == Direction.latitude {
            direction = (self.degrees < 0) ? "S" : "N"
        } else {
            direction = (self.degrees < 0) ? "W" : "E"
        }
        return "\(Float(self.degrees) + Float(self.minutes) / 60 + Float(self.seconds) / 3600)° \(direction)"
    }
    
    func midpoint(object: CoordinateDT) -> CoordinateDT? {
        return (self.direction == object.direction) ? CoordinateDT(direction: self.direction, degrees: (self.degrees + object.degrees) / 2, minutes: (self.minutes + object.minutes) / 2, seconds: (self.seconds + object.seconds) / 2) : nil
    }
    
    func midpoint(object1: CoordinateDT, object2: CoordinateDT) -> CoordinateDT? {
        return (object1.direction == object2.direction) ? CoordinateDT(direction: object1.direction, degrees: (object1.degrees + object2.degrees) / 2, minutes: (object1.minutes + object2.minutes) / 2, seconds: (object1.seconds + object2.seconds) / 2) : nil
    }
}

print()
var object1: CoordinateDT = CoordinateDT()
print(object1.string())
print(object1.decimalString())
print(object1.midpoint(object: CoordinateDT(direction: Direction.longitude, degrees: 30, minutes: 15, seconds: 50)) ?? "Different directions")
var object11: CoordinateDT? = object1.midpoint(object1: CoordinateDT(direction: Direction.longitude, degrees: 45, minutes: 45, seconds: 45), object2: CoordinateDT(direction: Direction.longitude, degrees: 55, minutes: 55, seconds: 55))
print("\(object11!.direction) \(object11!.degrees)°\(object11!.minutes)′​\(object11!.seconds)”")

print()
var object2: CoordinateDT = CoordinateDT(direction: Direction.latitude, degrees: 15, minutes: 77, seconds: 33)
print(object2.string())
print(object2.decimalString())
var object22: CoordinateDT? = object2.midpoint(object: CoordinateDT(direction: Direction.latitude, degrees: 15, minutes: 23, seconds: 47))
print("\(object22!.direction) \(object22!.degrees)°\(object22!.minutes)′​\(object22!.seconds)”")
print(object2.midpoint(object1: CoordinateDT(direction: Direction.longitude, degrees: 77, minutes: 77, seconds: 77), object2: CoordinateDT(direction: Direction.latitude, degrees: 33, minutes: 33, seconds: 33)) ?? "Different directions")
