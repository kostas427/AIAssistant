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
- Bubble style μηνύματα με timestamp
- Απαντάει πάντα στα ελληνικά

## Οδηγίες Εγκατάστασης

1. Κατέβασε το project
2. Άνοιξέ το στο Android Studio
3. Πρόσθεσε το OpenAI API key στο `app/src/main/res/values/secrets.xml`
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
