import { createFileRoute } from '@tanstack/react-router';
import MovementTypes from '@/features/movement-types';

export const Route = createFileRoute('/_authenticated/movement-types/')({
  component: MovementTypes,
});
