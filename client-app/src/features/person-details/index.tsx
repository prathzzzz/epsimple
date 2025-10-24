import { PersonDetailsProvider } from "./context/person-details-provider";
import { PersonDetailsTable } from "./components/person-details-table";
import { PersonDetailsDrawer } from "./components/person-details-drawer";
import { PersonDetailsDeleteDialog } from "./components/person-details-delete-dialog";
import { CreatePersonDetailsButton } from "./components/create-person-details-button";

export default function PersonDetailsPage() {
  return (
    <PersonDetailsProvider>
      <div className="flex h-full flex-1 flex-col space-y-8 p-8">
        <div className="flex items-center justify-between space-y-2">
          <div>
            <h2 className="text-2xl font-bold tracking-tight">Person Details</h2>
            <p className="text-muted-foreground">
              Manage all person details and their information
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreatePersonDetailsButton />
          </div>
        </div>
        <PersonDetailsTable />
        <PersonDetailsDrawer />
        <PersonDetailsDeleteDialog />
      </div>
    </PersonDetailsProvider>
  );
}
