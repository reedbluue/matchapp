abstract class TextUtil {
  public static getUpperCaseInitials(text: string): string {
    const names = text.split(" ");
    const mostSignificantNames = [names[0]];
    if (names.length > 1) mostSignificantNames.push(names[names.length - 1]);
    return mostSignificantNames.map(word => word.charAt(0).toUpperCase()).join("");
  }

  public static getSignificantNames(text: string): string {
    const names = text.split(" ");
    const mostSignificantNames = [names[0]];
    if (names.length > 1) mostSignificantNames.push(names[names.length - 1]);
    return mostSignificantNames.join(" ");
  }

  public static processFullName(fullName: string): string {
    try {
      const names = fullName.split(" ");
      return names.map(n => n[0].toUpperCase() + n.substring(1)).join(" ");
    } catch (e) {
      return fullName;
    }
  }
}

export default TextUtil;