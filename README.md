# Smart Storage AR (Android MVP)

Production-style Android MVP app in Kotlin + Jetpack Compose for digitizing storage spaces, capturing guided photos, managing nodes/items, searching inventory, and viewing AR labels.

## Tech Stack
- Kotlin, minSdk 28
- Jetpack Compose + Material 3
- MVVM + Repository pattern
- Room persistence
- Hilt DI
- CameraX capture flow
- ARCore availability + graceful fallback UI
- OpenCV blur detection and overlap validation
- Navigation Compose

## Implemented MVP Flow
1. **Home**: entry point with spaces list + navigation shortcuts.
2. **Create Space**: create a new storage space.
3. **Capture / Guidance**:
   - camera preview + capture button,
   - guidance text overlay,
   - blur rejection,
   - duplicate-angle rejection,
   - minimum 3 / maximum 7 photos.
4. **Review**: finalize scan.
5. **Mock modeling**: creates local demo `SpaceNode` records if cloud modeling is unavailable.
6. **Space detail**: list nodes + navigate to Add Item and AR.
7. **Add item**: bind item to node.
8. **Item detail**.
9. **Search** by item name or tags.
10. **AR viewer**: ARCore support check with fallback message.
11. **Settings** screen.

## Data Models
- `Space(id, name, coverImageUri, createdAt, updatedAt)`
- `SpaceNode(id, spaceId, parentId, type, name, centerX, centerY, centerZ, sizeX, sizeY, sizeZ, confidence)`
- `Item(id, name, imageUri, category, note, boundSpaceNodeId, createdAt, updatedAt)`
- `Scan(id, spaceId, status, createdAt)`

Node types: `room`, `cabinet`, `drawer`, `shelf`, `box`, `unknown`.

## Project Structure
- `app/src/main/java/com/smartstoragear/core`: db, repositories, DI, utilities.
- `app/src/main/java/com/smartstoragear/feature/*`: feature-specific ViewModels and screens.
- `app/src/main/java/com/smartstoragear/navigation`: Navigation graph + screen composables.

## Build & Run
1. Open in Android Studio (latest stable).
2. Ensure JDK 17 is selected for Gradle.
3. Sync project.
4. Run on emulator/device (API 28+).
5. Grant camera permission for capture flow.

## Notes
- AR page degrades gracefully when ARCore is unsupported.
- Room stores spaces, nodes, items, and scans.
- Capture validation rejects blurry and duplicate-angle images.

