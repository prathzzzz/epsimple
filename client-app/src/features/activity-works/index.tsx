import { ActivityWorkProvider } from './context/activity-work-provider';
import { ActivityWorkTable } from './components/activity-work-table';
import { CreateActivityWorkButton } from './components/create-activity-work-button';
import { ActivityWorkDrawer } from './components/activity-work-drawer';
import { ActivityWorkDeleteDialog } from './components/activity-work-delete-dialog';

export default function ActivityWorksPage() {
  return (
    <ActivityWorkProvider>
      <div className="flex h-full flex-col gap-4 p-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold tracking-tight">Activity Work Orders</h1>
            <p className="text-muted-foreground">
              Manage activity work orders and assignments
            </p>
          </div>
          <CreateActivityWorkButton />
        </div>

        <ActivityWorkTable />
        <ActivityWorkDrawer />
        <ActivityWorkDeleteDialog />
      </div>
    </ActivityWorkProvider>
  );
}
