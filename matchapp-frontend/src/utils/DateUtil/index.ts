abstract class DateUtil {
  static checkIfIsAdult(birthDay: Date): boolean {
    const newBirthDay = new Date(birthDay);
    newBirthDay.setFullYear(newBirthDay.getFullYear() + 18);
    return new Date() >= newBirthDay;
  }
}

export default DateUtil;