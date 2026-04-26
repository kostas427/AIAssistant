# CS Tutor AI - AI-Integrated Personal Assistant

Μια Android mobile εφαρμογή εκπαιδευτικού βοηθού για φοιτητές πληροφορικής, με ενσωμάτωση Τεχνητής Νοημοσύνης μέσω OpenAI API.

## Τεχνολογική Στοίβα

- **Frontend:** Android (Java)
- **IDE:** Android Studio
- **AI API:** OpenAI GPT-3.5-turbo
- **HTTP Client:** OkHttp3

## Λειτουργικότητα

- Chat με AI Tutor εξειδικευμένο σε θέματα πληροφορικής
- Απαντάει αποκλειστικά σε ερωτήσεις πληροφορικής
- Εμφανίζει "Σκέφτομαι..." ενώ επεξεργάζεται την ερώτηση
- Bubble style μηνύματα
- Απαντάει πάντα στα ελληνικά

## Οδηγίες Εγκατάστασης

1. Κατέβασε το project
2. Άνοιξέ το στο Android Studio
3. Δημιούργησε το αρχείο `app/src/main/res/values/secrets.xml` με το δικό σου OpenAI API key:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="openai_api_key">ΤΟ_API_KEY_ΣΟΥ_ΕΔΩ</string>
</resources>
```

4. Τρέξε την εφαρμογή σε emulator ή φυσικό κινητό

## Αρχιτεκτονική

Android App (Java)
↓
OkHttp3 HTTP Client
↓
OpenAI GPT-3.5-turbo API
↓
Απάντηση στον χρήστη

## API Διαχείριση

- Ασύγχρονες κλήσεις με OkHttp3 Callback
- Σωστός χειρισμός errors
- Loading indicator κατά την αναμονή

## Ασφάλεια

Το αρχείο `secrets.xml` δεν συμπεριλαμβάνεται στο αποθετήριο για λόγους ασφαλείας. Κάθε χρήστης πρέπει να δημιουργήσει το δικό του αρχείο με έγκυρο OpenAI API key από το https://platform.openai.com
