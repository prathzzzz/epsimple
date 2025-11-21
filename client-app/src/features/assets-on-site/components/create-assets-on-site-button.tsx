import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { PermissionGuard } from '@/components/permission-guard';
import { useAssetsOnSite } from '../context/assets-on-site-provider';

export function CreateAssetsOnSiteButton() {
  const { openDrawer } = useAssetsOnSite();

  return (
    <PermissionGuard permission="ASSETS_ON_SITE:CREATE">
      <Button onClick={openDrawer}>
        <Plus className="mr-2 h-4 w-4" />
        Place Asset on Site
      </Button>
    </PermissionGuard>
  );
}
