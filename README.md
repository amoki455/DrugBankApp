**Application Description: Drug Info Navigator**

**Overview:**
DrugBank App offers a user-friendly experience with a drug list, detailed drug information screens covering uses, dosage, and side effects, and interactive references with clickable superscript numbers. Users can access a bottom sheet dialog for reference details without leaving the information screen, and the action bar includes a dedicated button for quick reference access. This combination of features ensures that users can easily find and verify reliable medication information.

- **Technichal Information:**
  - **Code Architecture:** Clean Architecture MVVM
  - **UI System:** Android Views System
  - **Data Source:** Firebase firestore
  - **Dependency Injection:** Dagger Hilt
  - **Testing:** Junit4, Robolectric, Espresso
  - **Worth mentioning:** [Custom click handler for SpannedString with URLSpan](https://github.com/amoki455/DrugBankApp/blob/master/app/src/main/java/kishk/ahmedmohamed/drugbank/utils/TextViewLinkHandler.kt)

- **Key Features:**
  1. **Drug List Screen:**
     - The home screen displays a clean and organized list of drugs.
     - Each drug entry includes the drug name, a brief description.

  2. **Drug Info Screen:**
     - When a user clicks on a drug from the list, they are navigated to the Drug Info Screen.
     - This screen presents detailed information about the selected drug, including its uses, dosage, side effects, interactions, and contraindications.
     - The information is presented in a clear, easy-to-read format, with headings and bullet points for quick reference.

  3. **References and Citations:**
     - Each sentence in the information text that requires a reference is accompanied by a small superscript number.
     - Users can click on these reference numbers to open a bottom sheet dialog that displays the corresponding reference details, including the source of the information and any relevant notes.
  
  4. **Interactive Bottom Sheet Dialog:**
     - The bottom sheet dialog provides a seamless way to view references without navigating away from the Drug Info Screen.
     - Users can scroll through the list of references, which are organized numerically for easy access.
     - When a reference number is clicked, the dialog scrolls to the specific reference and highlights it, ensuring users can easily locate the information they need.
  
  5. **Action Bar Functionality:**
     - The action bar at the top of the Drug Info Screen includes a dedicated button that opens the bottom sheet dialog for references.
     - This button is easily accessible, allowing users to quickly check the sources of the information presented.

**User Experience:**
DrugBank App prioritizes user experience with its clean design and intuitive navigation. The app is designed to minimize distractions, allowing users to focus on the information they need. The interactive elements, such as clickable reference numbers and the bottom sheet dialog, enhance engagement and facilitate a deeper understanding of the material.
